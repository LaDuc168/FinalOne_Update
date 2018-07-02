package com.example.phamvolan.finalone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.phamvolan.finalone.model.DangKiNguoiDung;
import com.example.phamvolan.finalone.model.NguoiDung;

import java.util.HashMap;
import java.util.Map;

import ru.katso.livebutton.LiveButton;

public class DangKiActivity extends AppCompatActivity {

    EditText edtten,edtemail,edtpass,edtcomfirm;
    LiveButton btnDangnhap, btnHuy;

    ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        init();
        addEvent();

    }
    DangKiNguoiDung dk=null;
    private void addEvent() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten=edtten.getText().toString();
                String email=edtemail.getText().toString();
                String pass=edtpass.getText().toString();
                String confirm=edtcomfirm.getText().toString();

                dk=new DangKiNguoiDung(ten,email,pass);
                NguoiDung nd=new NguoiDung(ten,pass);
                if(!pass.equals(confirm)){
                    Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp.Xin kiểm tra lại", Toast.LENGTH_SHORT).show();
                    edtcomfirm.setText("");
                    return;
                }

                CheckTaiKhoanTrung(IPConnect.CHECK_TAI_KHOAN_TRUNG,nd);


            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void init() {

        edtten = (EditText) findViewById(R.id.edttendn);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtpass = (EditText) findViewById(R.id.edtpassword);
        edtcomfirm = (EditText) findViewById(R.id.edtconfirmpassword);
        btnDangnhap = (LiveButton) findViewById(R.id.btnDangNhap);
        btnHuy = (LiveButton) findViewById(R.id.btnHuy);
    }
    public void CheckTaiKhoanTrung(String url, final NguoiDung nd) {
        RequestQueue requestQueue = Volley.newRequestQueue(DangKiActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(DangKiActivity.this, "Tài khoản này đã có người đăng kí.", Toast.LENGTH_SHORT).show();


                } else {
                    DangKi(IPConnect.DANG_KI,dk);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DangKiActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", nd.getUsername());
                map.put("pass", nd.getPassword());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void DangKi(String url, final DangKiNguoiDung dk) {
        RequestQueue requestQueue = Volley.newRequestQueue(DangKiActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(DangKiActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DangKiActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(DangKiActivity.this,DangNhapActivity.class));
                finish();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DangKiActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> map=new HashMap<>();
                map.put("ten",dk.getTen());
                map.put("email",dk.getEmail());
                map.put("pass",dk.getPass());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }




}
