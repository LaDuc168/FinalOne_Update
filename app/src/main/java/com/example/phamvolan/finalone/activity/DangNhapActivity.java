package com.example.phamvolan.finalone.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.MainActivity;
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.ipaddress.Temp;
import com.example.phamvolan.finalone.model.NguoiDung;

import java.util.HashMap;
import java.util.Map;

import ru.katso.livebutton.LiveButton;

public class DangNhapActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    LiveButton btnDangnhap, btnHuy, btnXoa;

    ProgressDialog dialog = null;

    TextView txtXoaTK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        init();

        addEvent();
    }

//    private void addEvent() {
//        btnDangnhap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DangNhapActivity.this,DanhSachThanhPhoActivity.class));
//            }
//        });
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }

    private void init() {
        txtXoaTK = findViewById(R.id.txtxoatk);
        edtUsername = (EditText) findViewById(R.id.edtusername);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        btnDangnhap = (LiveButton) findViewById(R.id.btnDangNhap);
        btnHuy = (LiveButton) findViewById(R.id.btnHuy);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSign) {
            startActivity(new Intent(DangNhapActivity.this, DangKiActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dangki, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addEvent() {


        txtXoaTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(DangNhapActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.xoa_tai_khoan);
                dialog.setTitle("Xóa tài khoản");


                final EditText edtten = dialog.findViewById(R.id.edtten);
                final EditText edtmk = dialog.findViewById(R.id.edtmk);

                LiveButton btnXoa = dialog.findViewById(R.id.btnXoa);
                LiveButton btnCancel = dialog.findViewById(R.id.btnCancel);

                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = edtten.getText().toString();
                        String pass = edtmk.getText().toString();
                        NguoiDung nd = new NguoiDung(name, pass);
                        if (name.equals("") || pass.equals("")) {
                            Toast.makeText(DangNhapActivity.this, "Mời nhập tài khoản", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        XoaTaiKhoan(IPConnect.CHECK_TAI_KHOAN, nd, name);

                        dialog.dismiss();

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                NguoiDung nd = new NguoiDung(name, pass);
                if (name.equals("") || pass.equals("")) {
                    Toast.makeText(DangNhapActivity.this, "Mời nhập tài khoản", Toast.LENGTH_SHORT).show();
                    return;
                }

                CheckTaiKhoan(IPConnect.CHECK_TAI_KHOAN, nd);


            }
        });


    }

    public void CheckTaiKhoan(String url, final NguoiDung nd) {
        RequestQueue requestQueue = Volley.newRequestQueue(DangNhapActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Temp.CHECK_USER = nd.getUsername();
                    dialog = new ProgressDialog(DangNhapActivity.this);
                    dialog.setTitle("Đăng nhập");
                    dialog.setMessage("Đang xử lý....");
                    dialog.setCanceledOnTouchOutside(false);//click ra ngoài vẫn ko tắt
                    dialog.show();
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                            if (!TempAccount.ACCOUNT.equals(TempAccount.BAN_LANH_DAO) &&
//                                    !TempAccount.ACCOUNT.equals(TempAccount.NHAN_VIEN_TO_CHUC)){
                            startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
//                            }else
                        }
                    }.start();


                } else {
                    Toast.makeText(DangNhapActivity.this, "Tài khoản không đúng", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DangNhapActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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


    public void XoaTaiKhoan(String url, final NguoiDung nd, final String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(DangNhapActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
//                    Temp.CHECK_USER=nd.getUsername();
                    dialog = new ProgressDialog(DangNhapActivity.this);
                    dialog.setTitle("Xóa tài khoản " + name);
                    dialog.setMessage("Đang xử lý....");
                    dialog.setCanceledOnTouchOutside(false);//click ra ngoài vẫn ko tắt
                    dialog.show();
                    new CountDownTimer(4000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            XoaTaiKhoanOK(IPConnect.XOA_TAI_KHOAN,name);

                        }
                    }.start();


                } else {
                    Toast.makeText(DangNhapActivity.this, "Tài khoản không đúng", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DangNhapActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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


    public void XoaTaiKhoanOK(String url, final String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(DangNhapActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(DangNhapActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DangNhapActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> map=new HashMap<>();
                map.put("username",name);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
