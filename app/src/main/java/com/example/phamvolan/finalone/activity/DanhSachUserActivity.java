package com.example.phamvolan.finalone.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamvolan.finalone.MainActivity;
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.adapter.UserAdapter;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhSachUserActivity extends AppCompatActivity {

    ListView listViewUser;
    ArrayList<User> listUser;
    UserAdapter adapter;

    CircleImageView imgBack;
     View viewFooter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_user);
        init();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        viewFooter = inflater.inflate(R.layout.add_footer_view, null);
        listViewUser.addFooterView(viewFooter);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                listViewUser.removeFooterView(viewFooter);
                getDanhSachUser(IPConnect.GET_DACH_SACH_USER);
            }
        }.start();


        addEvent();

    }

    private void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachUserActivity.this, MainActivity.class));
                finish();
            }
        });


    }

    public void getDanhSachUser(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listUser.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String username = obj.getString("username");
                                String password = obj.getString("password");
                                String email = obj.getString("email");
                                listUser.add(new User(username, password, email));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter = new UserAdapter(DanhSachUserActivity.this, listUser);
                        listViewUser.setAdapter(adapter);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DanhSachUserActivity.this, "Loi Ok", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void init() {
        listViewUser = findViewById(R.id.listViewUser);
        listUser = new ArrayList<>();
        adapter = new UserAdapter(this, listUser);
        listViewUser.setAdapter(adapter);
        imgBack = findViewById(R.id.imgBack);
    }
}
