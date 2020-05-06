package com.sz.kejin.czfcs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.YcInfoBeans;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<YcInfoBeans> datas;
    private int type;

    public SpinnerAdapter(Context mContext, ArrayList<YcInfoBeans> datas, int type) {
        this.mContext = mContext;
        this.datas = datas;
        this.type = type;
    }

    public SpinnerAdapter() {
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.dzjc_fwjc_spinner_item, null);
        if (convertView != null) {
            TextView textView = convertView.findViewById(R.id.tv_fwhc_item_xm);
            switch (type) {
                case 0://省
                    textView.setText(datas.get(position).getSmc());
                    break;
                case 1://市
                    textView.setText(datas.get(position).getDjsmc());
                    break;
                case 2://区
                    textView.setText(datas.get(position).getQxmc());
                    break;
            }
        }
        return convertView;
    }
}
