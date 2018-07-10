package com.example.phamvolan.finalone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.phamvolan.finalone.adapter.ThanhPhoAdapter;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.ConstanDataManager;
import com.example.phamvolan.finalone.model.TramSelect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.katso.livebutton.LiveButton;

public class ThemTramActivity extends AppCompatActivity {


    Spinner spinner;
    EditText edtmatram,edttentram;
    LiveButton btnThem,btnHuy;

    String KHU_VUC_CHOOSED="";

    ArrayAdapter<String > adapterKhuVuc;
    ArrayList<String > mangKhuVuc;

    String RESULE_ACTIVITY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tram);
        init();
        RESULE_ACTIVITY=getIntent().getStringExtra(ConstanDataManager.VARIABLE_TRANSLATE);
        addEvent();

        getDanhSachKhuVuc(IPConnect.GET_DANH_SACH_TRAM);
    }

    private void addEvent() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index=mangKhuVuc.get(i).lastIndexOf("-");

                String s=mangKhuVuc.get(i).substring((index+1));

                KHU_VUC_CHOOSED = s;
                Toast.makeText(ThemTramActivity.this, "kv="+KHU_VUC_CHOOSED, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = edtmatram.getText().toString();
                String ten = edttentram.getText().toString();

                if(ma.equals("") || ten.equals("")){
                    Toast.makeText(ThemTramActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                TramSelect ts=new TramSelect(ma,KHU_VUC_CHOOSED,ten);

                ThemTram(IPConnect.THEM_TRAM,ts);

            }
        });
    }


    public void ThemTram(String url, final TramSelect ts) {
        RequestQueue requestQueue = Volley.newRequestQueue(ThemTramActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(ThemTramActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThemTramActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }

                if(RESULE_ACTIVITY.equals(ConstanDataManager.VALUE_GRAPH)){
                    startActivity(new Intent(ThemTramActivity.this,MainActivity.class));
                    finish();
                }else if(RESULE_ACTIVITY.equals(ConstanDataManager.VALUE_TABLE)){
                    startActivity(new Intent(ThemTramActivity.this,BangDiaChatActivity.class));
                    finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemTramActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> map=new HashMap<>();
                map.put("matramselect",ts.getMaTS());
                map.put("matram",ts.getMatram());
                map.put("tentram",ts.getTentram());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getDanhSachKhuVuc(String url) {
        mangKhuVuc.clear();

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

                                String tem=tentram+"-"+matram;
                                mangKhuVuc.add(tem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterKhuVuc=new ArrayAdapter<String >(ThemTramActivity.this,android.R.layout.simple_list_item_1,mangKhuVuc);
                        spinner.setAdapter(adapterKhuVuc);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemTramActivity.this, "Loi Ok", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void init() {
        spinner=findViewById(R.id.spinner);
        edtmatram=findViewById(R.id.edtmatram);
        edttentram=findViewById(R.id.edttentram);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        mangKhuVuc=new ArrayList<>();
        adapterKhuVuc=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mangKhuVuc);
        spinner.setAdapter(adapterKhuVuc);
    }
}
