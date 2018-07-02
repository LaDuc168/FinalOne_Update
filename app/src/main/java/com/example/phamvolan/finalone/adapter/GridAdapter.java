package com.example.phamvolan.finalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.phamvolan.finalone.R;

import java.util.List;

import ru.katso.livebutton.LiveButton;

public class GridAdapter extends BaseAdapter {
    Context myContext;
    List<String> lstSource;

    public GridAdapter(Context myContext, List<String> lstSource) {
        this.myContext = myContext;
        this.lstSource = lstSource;
    }

    @Override
    public int getCount() {
        return lstSource.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.row_item_grid,null);

        Button btn=convertView.findViewById(R.id.btnGrid);

        String value=lstSource.get(position);
        btn.setText(value);

        return convertView;
    }
}
