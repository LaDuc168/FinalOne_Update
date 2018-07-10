package com.example.phamvolan.finalone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.phamvolan.finalone.ipaddress.Temp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DanhSachThanhPhoActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> mangTP;

    ThanhPhoAdapter adapter;

    String MA_KHU_VUC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_thanh_pho);

        init();
        getDSTram(IPConnect.GET_DANH_SACH_TRAM);

        addEvent();

    }

    private void addEvent() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = mangTP.get(i).lastIndexOf("-");
                String s = mangTP.get(i).substring((index + 1));
                MA_KHU_VUC = s;

                registerForContextMenu(listView);
                return false;
            }
        });
    }


    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_list_view, menu);

        RequestQueue requestQueue = Volley.newRequestQueue(DanhSachThanhPhoActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IPConnect.GET_DS_TRAM_SELECTED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray object = null;
                try {
                    object = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < object.length(); i++) {
                    try {
                        JSONObject obj = object.getJSONObject(i);
                        String matram = obj.getString("matram");
                        String tentram = obj.getString("tentram");

                        menu.add(0, (i), 0, tentram);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                menu.setHeaderTitle("Chọn trạm");

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DanhSachThanhPhoActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("makv", MA_KHU_VUC);
                return map;
            }
        };
        requestQueue.add(stringRequest);

        //Co the set icon cho no menu.setHeaderIcon(truyen hinh vao);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qtmd:
                startActivity(new Intent(DanhSachThanhPhoActivity.this, MainActivity.class));
                break;
            case R.id.tthc:
                startActivity(new Intent(DanhSachThanhPhoActivity.this, MainActivity.class));

                break;
            case R.id.visp:
                startActivity(new Intent(DanhSachThanhPhoActivity.this, MainActivity.class));

                break;
        }
        return super.onContextItemSelected(item);
    }

//    private void addData() {
//        mangTP.add("Bình Dương New City");
//        mangTP.add("Thành Phố Thủ Dầu Một");
//        mangTP.add("Thị Xã Thuận An");
//        mangTP.add("Thị Xã Dĩ An");
//        mangTP.add("Thị Xã Bến Cát");
//    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        mangTP = new ArrayList<>();
        adapter = new ThanhPhoAdapter(this, mangTP);
        listView.setAdapter(adapter);

    }

    public void getDSTram(String url) {
        mangTP.clear();

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
                                mangTP.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter = new ThanhPhoAdapter(DanhSachThanhPhoActivity.this, mangTP);
                        listView.setAdapter(adapter);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DanhSachThanhPhoActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuthemkv) {
            startActivity(new Intent(DanhSachThanhPhoActivity.this, ThemKhuVucActivity.class));
            finish();
        }
        if (id == R.id.menuthemtram) {
            startActivity(new Intent(DanhSachThanhPhoActivity.this, ThemTramActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_them_tram, menu);
        getMenuInflater().inflate(R.menu.menu_them_kv, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (!Temp.CHECK_USER.equals("admin")) {

            menu.findItem(R.id.menuthemtram).setVisible(false);
            menu.findItem(R.id.menuthemkv).setVisible(false);
        }else {
            menu.findItem(R.id.menuthemtram).setVisible(true);
            menu.findItem(R.id.menuthemkv).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
