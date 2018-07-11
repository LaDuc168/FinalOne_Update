package com.example.phamvolan.finalone.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.example.phamvolan.finalone.R;
import com.example.phamvolan.finalone.activity.DangNhapActivity;
import com.example.phamvolan.finalone.activity.DanhSachUserActivity;
import com.example.phamvolan.finalone.ipaddress.IPConnect;
import com.example.phamvolan.finalone.model.User;
import com.github.library.bubbleview.BubbleTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LaVanDuc on 6/7/2018.
 */

public class UserAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<User> mang;

    public UserAdapter(Context myContext, ArrayList<User> mang) {
        this.myContext = myContext;
        this.mang = mang;
    }

    @Override
    public int getCount() {
        return mang.size();

    }

    public void setListUser(ArrayList<User> listuser){
        mang.addAll(listuser);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.row_items_user,null);

        ImageView imgDelete=view.findViewById(R.id.imgDelete);
        BubbleTextView txtContent=view.findViewById(R.id.txtContent);
        final User user=mang.get(i);

       String name=user.getUsername();
       String pass=user.getPassword();
       String email=user.getEmail();
       String chain="+ Username: "+name+"\n\n";
       chain+="+ Password: "+pass+"\n\n";
       chain+="+ Email: "+email;
       txtContent.setText(chain);

       imgDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder builder=new AlertDialog.Builder(myContext);
               builder.setTitle("Xác nhận");
               String s="Bạn muốn xóa user "+user.getUsername()+" không?";
               builder.setMessage(s);
               builder.setCancelable(false);
               builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
               builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       XoaTaiKhoanOK(IPConnect.XOA_TAI_KHOAN,user.getUsername());

                   }
               });
//        Dialog hopThoai=builder.create();
               builder.show();

           }
       });
        return view;
    }

    public void getDanhSachUser(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mang.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String username = obj.getString("username");
                                String password = obj.getString("password");
                                String email = obj.getString("email");
                                mang.add(new User(username,password,email));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        notifyDataSetChanged();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myContext, "Loi Ok", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void XoaTaiKhoanOK(String url, final String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(myContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(myContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
                getDanhSachUser(IPConnect.GET_DACH_SACH_USER);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myContext, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
