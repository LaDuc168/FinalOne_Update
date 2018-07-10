package com.example.phamvolan.finalone.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.MainActivity;
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.adapter.GridAdapter;
import com.example.phamvolan.finalone.adapter.ThanhPhoAdapter;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.ConstanDataManager;
import com.example.phamvolan.finalone.model.DateCompare;
import com.example.phamvolan.finalone.model.DateGraph;
import com.example.phamvolan.finalone.model.Date_Custom;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.katso.livebutton.LiveButton;

public class BangDiaChatActivity extends AppCompatActivity {

    List<String> list;
    GridView gridView;
    GridAdapter adapter;

    ProgressDialog dialog;
    EditText edtngayBD, edtngayKT;
    LiveButton btnOk;

    DateGraph dateGraph = null;
    private static int MY_TYPE = 0;

    Spinner spinner;
    ArrayList<String> mangSpinner;
    ArrayAdapter adapterSpinner;

    Spinner spinnerKV;
    ArrayList<String> mangSpinnerKV;
    ArrayAdapter adapterSpinnerKV;

    Spinner spinnerTram;
    ArrayList<String> mangSpinnerTram;
    ArrayAdapter adapterSpinnerTram;

    Date_Custom date_custom = null;
    TextView txtThongBao;

    private static final int NHIET_DO = 0;
    private static final int DO_AM = 1;
    private static final int CO = 2;
    private static final int BUI = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_dia_chat);
        init();
//        addData();

        addDataToSpinner();

        addEvent();
        getJSON(IPConnect.GET_DANH_SACH, MY_TYPE, dateGraph);
        getDSKhuVuc(IPConnect.GET_DANH_SACH_TRAM);
    }

    public void getDSKhuVuc(String url) {
        mangSpinnerKV.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String matram = obj.getString("matram");
                                String tentram = obj.getString("tentram");

                                String item = tentram + "-" + matram;
                                mangSpinnerKV.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterSpinnerKV = new ArrayAdapter(BangDiaChatActivity.this,
                                android.R.layout.simple_list_item_1, mangSpinnerKV);
                        spinnerKV.setAdapter(adapterSpinnerKV);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BangDiaChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void addDataToSpinner() {
        mangSpinner.add("Nhiệt độ");
        mangSpinner.add("Độ ẩm");
        mangSpinner.add("CO");
        mangSpinner.add("Bụi");
        adapterSpinner.notifyDataSetChanged();
    }

    private void addEvent() {
        spinnerKV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = mangSpinnerKV.get(i).lastIndexOf("-");
                String s = mangSpinnerKV.get(i).substring((index + 1));
                getDSTramSelected(IPConnect.GET_DS_TRAM_SELECTED,s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinnerTram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int index = mangSpinnerKV.get(i).lastIndexOf("-");
//                String s = mangSpinnerKV.get(i).substring((index + 1));

                if(!mangSpinnerTram.get(i).equals(ConstanDataManager.CONSTAN_TRAM)){
//                    edtngayBD.setText("");
//                    edtngayKT.setText("");
                    txtThongBao.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                }else {
                    txtThongBao.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }
//                getDSTramSelected(IPConnect.GET_DS_TRAM_SELECTED,s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtngayBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(BangDiaChatActivity.this);
                dialog.setTitle("Dialog chọn ngày-tháng-năm");
                dialog.setContentView(R.layout.dialog_item_ngay_thang);
                dialog.show();
                LiveButton btnChonNgay = dialog.findViewById(R.id.btnChonNgay);
                final DatePicker datePicker = dialog.findViewById(R.id.datePickerOK);
                btnChonNgay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int nam = datePicker.getYear();
                        int thang = datePicker.getMonth() + 1;
                        int ngay = datePicker.getDayOfMonth();

                        String day = "", month = "";

                        if (ngay < 10) {
                            day = "0" + ngay;
                        } else {
                            day = ngay + "";
                        }
                        if (thang < 10) {
                            month = "0" + thang;
                        } else {
                            month = thang + "";
                        }

                        String s = nam + "-" + month + "-" + day;


                        edtngayBD.setText(s);
                        dialog.dismiss();
                    }
                });
            }

        });


        edtngayKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(BangDiaChatActivity.this);
                dialog.setTitle("Dialog chọn ngày-tháng-năm");
                dialog.setContentView(R.layout.dialog_item_ngay_thang);
                dialog.show();
                LiveButton btnChonNgay = dialog.findViewById(R.id.btnChonNgay);
                final DatePicker datePicker = dialog.findViewById(R.id.datePickerOK);
                btnChonNgay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int nam = datePicker.getYear();
                        int thang = datePicker.getMonth() + 1;
                        int ngay = datePicker.getDayOfMonth();

                        String day = "", month = "";

                        if (ngay < 10) {
                            day = "0" + ngay;
                        } else {
                            day = ngay + "";
                        }
                        if (thang < 10) {
                            month = "0" + thang;
                        } else {
                            month = thang + "";
                        }

                        String s = nam + "-" + month + "-" + day;


                        edtngayKT.setText(s);
                        dialog.dismiss();
                    }
                });
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MY_TYPE = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngayBD = edtngayBD.getText().toString();
                String ngayKT = edtngayKT.getText().toString();


                if (ngayBD.equals("") || ngayKT.equals("")) {
                    Toast.makeText(BangDiaChatActivity.this, "Mời nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }


                DateGraph dg = new DateGraph(ngayBD, ngayKT);
                dateGraph = dg;
                getJSON(IPConnect.GET_DANH_SACH, MY_TYPE, dg);

            }
        });
    }

    private void getDSTramSelected(String url, final String makv){
        mangSpinnerTram.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(BangDiaChatActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray object = null;
                try {
                    object = new JSONArray(response);
                } catch (JSONException e) {
                    Toast.makeText(BangDiaChatActivity.this, "Error parse Json", Toast.LENGTH_SHORT).show();
                }


                for (int i = 0; i < object.length(); i++) {
                    try {
                        JSONObject obj = object.getJSONObject(i);
                        String matram = obj.getString("matram");
                        String tentram = obj.getString("tentram");

                        mangSpinnerTram.add(tentram);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(mangSpinnerTram.size()==0 || !ConstanDataManager.CheckTram(mangSpinnerTram)){
//                    edtngayBD.setText("");
//                    edtngayKT.setText("");
                    txtThongBao.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                }else {
                    txtThongBao.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }
                adapterSpinnerTram = new ArrayAdapter(BangDiaChatActivity.this,
                        android.R.layout.simple_list_item_1, mangSpinnerTram);
                spinnerTram.setAdapter(adapterSpinnerTram);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BangDiaChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("makv", makv);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }




    private void init() {
        txtThongBao=findViewById(R.id.txtThongBao);
        edtngayBD = findViewById(R.id.edtngayBD);
        edtngayKT = findViewById(R.id.edtngayKT);
        btnOk = findViewById(R.id.btnOk);
        list = new ArrayList<>();
        gridView = findViewById(R.id.grid);
        adapter = new GridAdapter(this, list);
        gridView.setAdapter(adapter);

        //Set date to spinner
        spinner = findViewById(R.id.spinner);
        mangSpinner = new ArrayList<>();
        adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mangSpinner);
        spinner.setAdapter(adapterSpinner);

        // Spinner Khu vực
        spinnerKV = findViewById(R.id.spinnerKV);
        mangSpinnerKV = new ArrayList<>();
        adapterSpinnerKV = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mangSpinnerKV);
        spinnerKV.setAdapter(adapterSpinnerKV);
        //Spinner Trạm
        spinnerTram = findViewById(R.id.spinnerTram);
        mangSpinnerTram = new ArrayList<>();
        adapterSpinnerTram = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mangSpinnerTram);
        spinnerTram.setAdapter(adapterSpinnerTram);
    }


    public void getJSON(final String url, final int type, final DateGraph dg) {
        dialog = new ProgressDialog(BangDiaChatActivity.this);
        dialog.setTitle("Vui lòng chờ");
        dialog.setMessage("Đang xử lý....");
        dialog.setCanceledOnTouchOutside(false);//click ra ngoài vẫn ko tắt
        dialog.show();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.cancel();

                RequestQueue requestQueue = Volley.newRequestQueue(BangDiaChatActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                list.clear();
                                if (type == NHIET_DO) {
                                    list.add("timeStamp");
                                    list.add("Nhiệt độ");


                                } else if (type == DO_AM) {
                                    list.add("timeStamp");
                                    list.add("Độ ẩm");

                                } else if (type == CO) {
                                    list.add("timeStamp");
                                    list.add("CO");

                                } else if (type == BUI) {
                                    list.add("timeStamp");
                                    list.add("Bụi");
                                }


                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject obj = response.getJSONObject(i);
                                        String matram = obj.getString("timeStamp");
                                        double tentram = 0;

                                        if (dg != null) {
                                                DateCompare dateCompare = getDateCompare(matram);
                                                Date_Custom ct = new Date_Custom(dg.getDateBD(), dg.getDateKT());

                                                if (ct.ComparDate(dateCompare.getDate())) {

                                                    if (type == NHIET_DO) {
                                                        tentram = obj.getDouble("temperature");

                                                    } else if (type == DO_AM) {
                                                        tentram = obj.getDouble("humidity");
                                                    } else if (type == CO) {
                                                        tentram = obj.getDouble("co");
                                                    } else if (type == BUI) {
                                                        tentram = obj.getDouble("bui");
                                                    }
                                                    list.add(matram);
                                                    list.add(tentram + "");

                                                }
                                        } else if (dg == null) {
                                            DateCompare dateCompare = getDateCompare(matram);
                                            if (type == NHIET_DO) {
                                                tentram = obj.getDouble("temperature");

                                            } else if (type == DO_AM) {
                                                tentram = obj.getDouble("humidity");
                                            } else if (type == CO) {
                                                tentram = obj.getDouble("co");
                                            } else if (type == BUI) {
                                                tentram = obj.getDouble("bui");
                                            }
                                            list.add(matram);
                                            list.add(tentram + "");

                                        }

                                        adapter = new GridAdapter(BangDiaChatActivity.this, list);
                                        gridView.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(BangDiaChatActivity.this, "Loi Ok", Toast.LENGTH_LONG).show();
                            }
                        }
                );

                requestQueue.add(jsonArrayRequest);


            }
        }.start();


    }

    private DateCompare getDateCompare(String value) {
        int x = value.indexOf(" ");
        String my_date = value.substring(0, x);

        int index_hour = value.indexOf(":");
        String hour = value.substring(x + 1, index_hour);
        int number_hour = Integer.parseInt(hour);
//       number_hour++;

        return new DateCompare(my_date, number_hour);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuthemupdate) {
            final String mangItems[]={"Thêm khu vực","Thêm trạm"};
            AlertDialog.Builder builder=new AlertDialog.Builder(BangDiaChatActivity.this);
            builder.setTitle("Xác nhận");
            builder.setIcon(R.drawable.add);
            builder.setCancelable(false);
            builder.setSingleChoiceItems(mangItems, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ConstanDataManager.SELECT_ITEM_DIALOG=which;
                }
            });

            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(ConstanDataManager.SELECT_ITEM_DIALOG==ConstanDataManager.SELECT_THEM_KHU_VUC){
                        Intent intent=new Intent(BangDiaChatActivity.this, ThemKhuVucActivity.class);
                        intent.putExtra(ConstanDataManager.VARIABLE_TRANSLATE,ConstanDataManager.VALUE_TABLE);
                        startActivity(intent);
                        finish();
                    }else if(ConstanDataManager.SELECT_ITEM_DIALOG==ConstanDataManager.SELECT_THEM_TRAM){
                        Intent intent=new Intent(BangDiaChatActivity.this, ThemTramActivity.class);
                        intent.putExtra(ConstanDataManager.VARIABLE_TRANSLATE,ConstanDataManager.VALUE_TABLE);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }

        if (id == R.id.menudangxuat) {

            dialog = new ProgressDialog(BangDiaChatActivity.this);
            dialog.setTitle("Đăng xuất");
            dialog.setMessage("Đang xử lý....");
            dialog.setCanceledOnTouchOutside(false);//click ra ngoài vẫn ko tắt
            dialog.show();

            new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.cancel();
                    startActivity(new Intent(BangDiaChatActivity.this, DangNhapActivity.class));
                    finish();
                    Toast.makeText(BangDiaChatActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

                }
            }.start();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_them_update, menu);
        getMenuInflater().inflate(R.menu.menu_dang_xuat, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
