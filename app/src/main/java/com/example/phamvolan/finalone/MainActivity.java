package com.example.phamvolan.finalone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.activity.BangDiaChatActivity;
import com.example.phamvolan.finalone.activity.DangNhapActivity;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.DateCompare;
import com.example.phamvolan.finalone.model.DateGraph;
import com.example.phamvolan.finalone.model.MyDate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ru.katso.livebutton.LiveButton;

public class MainActivity extends AppCompatActivity {

//    public static final String IP = "192.168.1.17";

    GraphView graph;

    ArrayList<Double> mangX;
    ArrayList<Double> mangY;
    String[] LabelX;
    String[] LabelY;
    MyDate myDate = null;
    MyDate myDateLast = null;


    private static final int NHIET_DO = 0;
    private static final int DO_AM = 1;
    private static final int CO = 2;
    private static final int BUI = 3;


    ProgressDialog dialog = null;

    //Khai báo biến ngày giờ

    EditText edtngay, edtgiobatdau, edtgioketthuc;
    LiveButton btnOk;

    DateGraph dateGraph = null;
    private static int MY_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getJSON(IPConnect.GET_DANH_SACH, NHIET_DO, dateGraph);

        addEvent();

    }

    private void addEvent() {
        edtngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
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



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngay = edtngay.getText().toString();

                String bd = edtgiobatdau.getText().toString();
                String kt = edtgioketthuc.getText().toString();


                if (ngay.equals("") || bd.equals("") || kt.equals("")) {
                    Toast.makeText(MainActivity.this, "Mời nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                int gioBD = Integer.parseInt(bd);
                int gioKT = Integer.parseInt(kt);


                DateGraph dg = new DateGraph(ngay, gioBD, gioKT);
                dateGraph = dg;
                getJSON(IPConnect.GET_DANH_SACH, MY_TYPE, dg);

            }
        });

    }

    private void init() {
        edtngay = findViewById(R.id.edtngay);
        edtgiobatdau = findViewById(R.id.edtgiobatdau);
        edtgioketthuc = findViewById(R.id.edtgioketthuc);
        btnOk = findViewById(R.id.btnOk);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menudangxuat) {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Đăng xuất");
            dialog.setMessage("Đang xử lý....");
            dialog.setCanceledOnTouchOutside(false);//click ra ngoài vẫn ko tắt
            dialog.show();

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.cancel();
                    startActivity(new Intent(MainActivity.this, DangNhapActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

                }
            }.start();


        }

        if (id == R.id.menunhietdo) {
            MY_TYPE = NHIET_DO;
            dateGraph = null;
            resetEdittext();
            Toast.makeText(this, "Đồ thị nhiệt độ", Toast.LENGTH_SHORT).show();
            getJSON(IPConnect.GET_DANH_SACH, NHIET_DO, dateGraph);

        }

        if (id == R.id.menudoam) {
            MY_TYPE = DO_AM;
            dateGraph = null;
            resetEdittext();
            Toast.makeText(this, "Đồ thị độ ẩm", Toast.LENGTH_SHORT).show();
            getJSON(IPConnect.GET_DANH_SACH, DO_AM, dateGraph);

        }

        if (id == R.id.menuco) {
            MY_TYPE = CO;
            dateGraph = null;
            resetEdittext();
            Toast.makeText(this, "Đồ thị nồng độ khí CO", Toast.LENGTH_SHORT).show();
            getJSON(IPConnect.GET_DANH_SACH, CO, dateGraph);

        }
        if (id == R.id.menubui) {
            MY_TYPE = BUI;
            dateGraph = null;
            resetEdittext();
            Toast.makeText(this, "Đồ thị nồng độ bụi", Toast.LENGTH_SHORT).show();
            getJSON(IPConnect.GET_DANH_SACH, BUI, dateGraph);

        }
        if (id == R.id.menutable) {

           startActivity(new Intent(MainActivity.this, BangDiaChatActivity.class));


        }


        return super.onOptionsItemSelected(item);
    }

    private void resetEdittext() {
        edtngay.setText("");
        edtgiobatdau.setText("");
        edtgioketthuc.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nhiet_do, menu);
        getMenuInflater().inflate(R.menu.menu_do_am, menu);
        getMenuInflater().inflate(R.menu.menu_co, menu);
        getMenuInflater().inflate(R.menu.menu_bui, menu);
        getMenuInflater().inflate(R.menu.menu_table, menu);
        getMenuInflater().inflate(R.menu.menu_dang_xuat, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void VeGraph(String[] TrucY) {


//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                new DataPoint(7, 110),
//                new DataPoint(8, 180),
//                new DataPoint(9, 125),
//                new DataPoint(10, 123),
//                new DataPoint(11, 123)
//        });
//        graph.addSeries(series);


//        LineGraphSeries<DataPoint> series = null;
//
        DataPoint[] mangTemp = new DataPoint[mangX.size()];
//
////        Collections.sort(mangX);
////
        for (int i = 0; i < mangX.size(); i++) {
            mangTemp[i] = new DataPoint(mangX.get(i), mangY.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(mangTemp);
////
//        series = new LineGraphSeries<DataPoint>(mangTemp
//        );
//////        graph.refreshDrawableState();
////
        graph.removeAllSeries();
        graph.addSeries(series);
        // use static labels for horizontal and vertical labels

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        staticLabelsFormatter.setHorizontalLabels(init_Label_X());

        staticLabelsFormatter.setVerticalLabels(TrucY);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    public String[] init_Label_CO() {

        ArrayList<Integer> m = new ArrayList<>();
        for (int i = 100; i <= 500; i += 50) {
            m.add(i);
        }
        LabelY = new String[m.size()];

        for (int i = 0; i < m.size(); i++) {
            LabelY[i] = m.get(i) + " ppm";

        }
        return LabelY;
    }


    public String[] init_Label_Nhiet_Do() {

        ArrayList<Integer> m = new ArrayList<>();
        for (int i = 30; i <= 100; i += 10) {
            m.add(i);
        }
        LabelY = new String[m.size()];

        for (int i = 0; i < m.size(); i++) {
            LabelY[i] = m.get(i) + " °C";

        }
        return LabelY;
    }


    public String[] init_Label_Do_Am() {

        ArrayList<Integer> m = new ArrayList<>();
        for (int i = 10; i <= 90; i += 10) {
            m.add(i);
        }
        LabelY = new String[m.size()];

        for (int i = 0; i < m.size(); i++) {
            LabelY[i] = m.get(i) + " %";

        }
        return LabelY;
    }

    public String[] init_Label_Bui() {

        ArrayList<Integer> m = new ArrayList<>();
        for (int i = 50; i <= 300; i += 50) {
            m.add(i);
        }
        LabelY = new String[m.size()];

        for (int i = 0; i < m.size(); i++) {
            LabelY[i] = m.get(i) + " ppm";

        }
        return LabelY;
    }

    public String[] init_Label_X() {
//        int num = 30 - myDate.getDate();
//        LabelX = new String[num];
//
//
//        int index = 0;
//        for (int i = myDate.getDate(); i < 30; i++) {
//            LabelX[index] = i + "/" + myDate.getMonth();
//            index++;
//            if (index > 8) break;
//        }

        if (dateGraph != null) {
            if (!dateGraph.getDate().equals("")) {

                int count = dateGraph.getGioKT() - dateGraph.getGioBD();
                LabelX = new String[count + 1];
                int index = 0;
                for (int i = dateGraph.getGioBD(); i <= dateGraph.getGioKT(); i++) {
                    LabelX[index++] = i + "";
                }
            }
        } else if (dateGraph == null) {
            LabelX = new String[18];
            int index = 0;
            for (int i = 1; i <= 18; i++) {
                LabelX[index++] = i + "";
            }
        }


        return LabelX;
    }


    public void getJSON(final String url, final int type, final DateGraph dg) {

        dialog = new ProgressDialog(MainActivity.this);
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

                graph = (GraphView) findViewById(R.id.graph);

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                LineGraphSeries<DataPoint> series = null;
//                                String[] ngang = new String[response.length()];
                                mangX = new ArrayList<>();
                                mangY = new ArrayList<>();
                                LabelX = new String[response.length()];


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
                                                        mangX.add((double) dateCompare.getGio());
                                                        Toast.makeText(MainActivity.this, "gio="+dateCompare.getGio(), Toast.LENGTH_SHORT).show();

                                                        if (type == NHIET_DO) {
                                                            tentram = obj.getDouble("temperature");

                                                        } else if (type == DO_AM) {
                                                            tentram = obj.getDouble("humidity");
                                                        } else if (type == CO) {
                                                            tentram = obj.getDouble("co");
                                                        } else if (type == BUI) {
                                                            tentram = obj.getDouble("bui");
                                                        }
                                                        mangY.add(tentram);

                                                    }
                                                }
                                            }
                                        }else if(dg==null){
                                            DateCompare dateCompare = getDateCompare(matram);
                                            mangX.add((double) dateCompare.getGio());
                                            if (type == NHIET_DO) {
                                                tentram = obj.getDouble("temperature");

                                            } else if (type == DO_AM) {
                                                tentram = obj.getDouble("humidity");
                                            } else if (type == CO) {
                                                tentram = obj.getDouble("co");
                                            } else if (type == BUI) {
                                                tentram = obj.getDouble("bui");
                                            }
                                            mangY.add(tentram);

                                        }


//                                        int numberOfString = getNumberOfString(matram);
//
//                                        if (i == 0)
//                                            myDate = GetDate(matram);
//
//                                        if (i == (response.length() - 1)) {
//                                            myDateLast = GetDate(matram);
//                                        }
//                                        Toast.makeText(MainActivity.this, "m="+numberOfString, Toast.LENGTH_SHORT).show();
//
//                                        mangX.add((double) numberOfString);
//                                        mangY.add(tentram);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (type == NHIET_DO) {
                                    VeGraph(init_Label_Nhiet_Do());
                                } else if (type == DO_AM) {
                                    VeGraph(init_Label_Do_Am());
                                } else if (type == CO) {
                                    VeGraph(init_Label_CO());
                                } else if (type == BUI) {
                                    VeGraph(init_Label_Bui());
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Loi Ok", Toast.LENGTH_LONG).show();
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

    public static MyDate GetDate(String value) {
        String h[] = value.split(" ");

        int first = h[0].indexOf("-");
        int last = h[0].lastIndexOf("-");

        String m = value.substring((first + 1), last);
        String d = h[0].substring((last + 1));

        int x = Integer.parseInt(m);
        int y = Integer.parseInt(d);

        MyDate mydate = new MyDate(y, x);
        return mydate;
    }

    public int getNumberOfString(String value) {
        int first = value.indexOf(":");
        int last = value.lastIndexOf(":");

        String s = value.substring((first + 1), last);

        int x = Integer.parseInt(s);
        return x;
    }
}
