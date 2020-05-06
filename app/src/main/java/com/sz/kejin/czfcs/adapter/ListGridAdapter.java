package com.sz.kejin.czfcs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sz.kejin.czfcs.R;

import java.util.List;


public class ListGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> listItems;
    public ListGridAdapter(Context context, List<String> listItems){
        this.context = context;
        this.listItems = listItems;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_grid_adapter,null);
        TextView textView = (TextView)convertView.findViewById(R.id.text_list_grid_adapter);
        textView.setText(listItems.get(position));
        return convertView;
    }
}
