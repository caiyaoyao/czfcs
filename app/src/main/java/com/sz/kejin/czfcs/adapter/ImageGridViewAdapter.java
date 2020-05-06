package com.sz.kejin.czfcs.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.utils.GetImageUtils;


import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class ImageGridViewAdapter extends BaseAdapter {
    private final Context context;
    private List<String> list;
    private DeleteItem deleteItem;
    private AddItem addItem;
    private ChoeseItem chose;
    public int type=1;


    public ImageGridViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public ImageGridViewAdapter(Context context, List<String> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {

       if(type==1){
           if(list.size()>=10000){
               return 10000;
           }else{
               return list.size()+1;
           }
       }else {
           return list.size();
       }

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context,  R.layout.image_grid_layout_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deleteItem!=null){
                    deleteItem.delete(position);
                }

            }
        });

        if(position==list.size()){
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addItem!=null){
                        if (list.size() >= GetImageUtils.UPLOAD_MAX_NUM) {
                            Toast.makeText(context, "每次最多上传" + GetImageUtils.UPLOAD_MAX_NUM +  "张图片", Toast.LENGTH_SHORT).show();
                        } else {
                            addItem.add(position);
                        }
                    }

                }
            });
        }else {
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chose!=null){
                        chose.chose(position);
                    }

                }
            });
        }

        if(type==1){
            if(position<list.size()){//有图有删除按钮
                startTh(holder, position);
            }else {//无图无删除按钮
                holder.btnDelete.setVisibility(View.GONE);
                Glide.with(context).load("")
                        .into(holder.ivAdd);
                holder.ivAdd.setImageResource(R.mipmap.ic_add_photo);
            }
        }else {
            holder.btnDelete.setVisibility(View.GONE);

            String path = list.get(position);
            if(path.endsWith("mp4")){
                Glide.with(context).load(R.mipmap.shipin)
                        .override(240,240)
                        .placeholder(R.drawable.white_corner_6_shape)
                        .into(holder.ivAdd);

            }else{
                Glide.with(context).load(OkHttpHelper.ROOT + path)
                        .override(240,240)
                        .placeholder(R.drawable.white_corner_6_shape)
                        .into(holder.ivAdd);
            }
        }

        return convertView;
    }



    //设置图片
    private void startTh(final Holder holder, final int position) {
        String path = list.get(position);
        if(path.endsWith("mp4")){
            Glide.with(context).load(R.mipmap.shipin)
                    .override(240,240)
                    .into(holder.ivAdd);

        }else{
            String str = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!path.contains(str)) {
                path = OkHttpHelper.ROOT + path;
            }
            Glide.with(context).load(path)
                    .override(240,240)
                    .into(holder.ivAdd);
        }

        holder.btnDelete.setVisibility(View.VISIBLE);

    }



    static class Holder {

        private final ImageView ivAdd;
        private final Button btnDelete;

        public Holder(View itemV) {
            ivAdd = (ImageView) itemV.findViewById(R.id.btn_add_photo);
            btnDelete = (Button) itemV.findViewById(R.id.btn_delete_photo);
        }
    }
    //指纹
    public interface DeleteItem {
        void delete(int postion);
    }

    public void deleteItem(DeleteItem deleteItem) {

        this.deleteItem = deleteItem;
    }

    public interface AddItem {
        void add(int postion);
    }

    public void addItem(AddItem addItem) {

        this.addItem = addItem;
    }

    public interface ChoeseItem {
        void chose(int postion);
    }

    public void choseItem(ChoeseItem chose) {


        this.chose = chose;
    }


}
