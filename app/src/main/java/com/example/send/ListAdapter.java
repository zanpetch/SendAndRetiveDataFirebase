package com.example.send;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<Data> listDatas;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context, List<Data> listDatas){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listDatas = listDatas;
    }
    @Override
    public int getCount() {
        if (listDatas == null){
            return 0;
        }
        return listDatas.size();
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
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item,parent,false);

        TextView nameData = (TextView) view.findViewById(R.id.nameItem);
        TextView idData = (TextView) view.findViewById(R.id.idItem);

        nameData.setText(listDatas.get(position).getName());
        idData.setText(""+listDatas.get(position).getId());

        return view;
    }
}
