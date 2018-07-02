package com.example.phamvolan.finalone.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.MainActivity;
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.adapter.GridAdapter;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.DateCompare;
import com.example.phamvolan.finalone.model.DateGraph;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.katso.livebutton.LiveButton;

public class BangDiaChatActivity extends AppCompatActivity {

    List<String> list;
    GridView gridView;
    GridAdapter adapter;

    ProgressDialog dialog;
    EditText edtngay, edtgiobatdau, edtgioketthuc;
    LiveButton btnOk;

    DateGraph dateGraph = null;
    private static int MY_TYPE = 0;

    Spinner spinner;

    ArrayList<String> mangSpinner;
    ArrayAdapter adapterSpinner;

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
        getJSON(IPConnect.GET_DANH_SACH, MY_TYPE,dateGraph);
    }

    private void addDataToSpinner() {
        mangSpinner.add("Nhiệt độ");
        mangSpinner.add("Độ ẩm");
        mangSpinner.add("CO");
        mangSpinner.add("Bụi");
        adapterSpinner.notifyDataSetChanged();
    }

    private void addEvent() {

        edtngay.setOnClickListener(new View.OnClickListener() {
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

                        String day="",month="";

                        if(ngay<10){
                            day="0"+ngay;
                        }else {
                            day=ngay+"";
                        }
                        if(thang<10){
                            month="0"+thang;
                        }else {
                            month=thang+"";
                        }

                        String s=nam+"-"+month+"-"+day;



                        edtngay.setText(s);
                        dialog.dismiss();
                    }
                });
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MY_TYPE=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngay = edtngay.getText().toString();

                String bd = edtgiobatdau.getText().toString();
                String kt = edtgioketthuc.getText().toString();


                if (ngay.equals("") || bd.equals("") || kt.equals("")) {
                    Toast.makeText(BangDiaChatActivity.this, "Mời nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                int gioBD = Integer.parseInt(bd);
                int gioKT = Integer.parseInt(kt);


                DateGraph dg = new DateGraph(ngay, gioBD, gioKT);
                dateGraph = dg;
                getJSON(IPConnect.GET_DANH_SACH, MY_TYPE,dg);

            }
        });
    }

//    private void addData() {
//        for (int i = 0; i < 20; i++) {
//            list.add(i+"la van duc la do something");
//
//        }
//        adapter.notifyDataSetChanged();
//    }

    private void init() {
        edtngay = findViewById(R.id.edtngay);
        edtgiobatdau = findViewById(R.id.edtgiobatdau);
        edtgioketthuc = findViewById(R.id.edtgioketthuc);
        btnOk = findViewById(R.id.btnOk);
        list=new ArrayList<>();
        gridView=findViewById(R.id.grid);
        adapter=new GridAdapter(this,list);
        gridView.setAdapter(adapter);

        //Set date to spinner
        spinner=findViewById(R.id.spinner);
        mangSpinner=new ArrayList<>();
        adapterSpinner=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mangSpinner);
        spinner.setAdapter(adapterSpinner);

    }



    public void getJSON(final String url,final int type, final DateGraph dg) {

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
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject obj = response.getJSONObject(i);
                                        String matram = obj.getString("timeStamp");
                                        double tentram = 0;

                                        if (dg != null) {
                                            if (!dg.getDate().equals("")) {

                                                DateCompare dateCompare = getDateCompare(matram);

                                                if (dateCompare.getDate().equals(dg.getDate())) {
                                                    if (dateCompare.getGio() >= dg.getGioBD() && dateCompare.getGio() <= dg.getGioKT()) {



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
                                                        list.add(tentram+"");

                                                    }
                                                }
                                            }
                                        }else if(dg==null){
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
                                            list.add(tentram+"");

                                        }

                                        adapter=new GridAdapter(BangDiaChatActivity.this,list);
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
}
