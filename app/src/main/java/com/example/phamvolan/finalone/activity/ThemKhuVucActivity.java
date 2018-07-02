package com.example.phamvolan.finalone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.KhuVuc;

import java.util.HashMap;
import java.util.Map;

import ru.katso.livebutton.LiveButton;

public class ThemKhuVucActivity extends AppCompatActivity {


    EditText edtmakv, edttenkv;
    LiveButton btnThem, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_khu_vuc);

        init();

        addEvent();

    }

    private void addEvent() {
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = edtmakv.getText().toString();
                String ten = edttenkv.getText().toString();

                if(ma.equals("") || ten.equals("")){
                    Toast.makeText(ThemKhuVucActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                KhuVuc kv=new KhuVuc(ma,ten);

                ThemKhuVuc(IPConnect.THEM_KHU_VUC,kv);

            }
        });
    }


    public void ThemKhuVuc(String url, final KhuVuc kv) {
        RequestQueue requestQueue = Volley.newRequestQueue(ThemKhuVucActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(ThemKhuVucActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThemKhuVucActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(ThemKhuVucActivity.this,DanhSachThanhPhoActivity.class));
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThemKhuVucActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("makv", kv.getMa());
                map.put("tenkv", kv.getTen());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void init() {
        edtmakv = findViewById(R.id.edtmakv);
        edttenkv = findViewById(R.id.edttenkv);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);
    }
}
