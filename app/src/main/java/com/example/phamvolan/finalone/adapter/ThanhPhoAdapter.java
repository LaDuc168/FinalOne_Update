package com.example.phamvolan.finalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.phamvolan.finalone.R;

import java.util.ArrayList;

/**
 * Created by LaVanDuc on 6/7/2018.
 */

public class ThanhPhoAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<String > mang;

    public ThanhPhoAdapter(Context myContext, ArrayList<String> mang) {
        this.myContext = myContext;
        this.mang = mang;
    }

    @Override
    public int getCount() {
        return mang.size();

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
        view=inflater.inflate(R.layout.row_item_thanh_pho,null);
        TextView txtThanhPho= (TextView) view.findViewById(R.id.txtthanhpho);
        TextView txtChonTram= (TextView) view.findViewById(R.id.txtchontram);

        String item=mang.get(i);

        int index=item.lastIndexOf("-");
        String s=item.substring(0,(index));
        txtThanhPho.setText(s);
        return view;
    }
}
