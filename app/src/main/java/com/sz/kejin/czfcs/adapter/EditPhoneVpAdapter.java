package com.sz.kejin.czfcs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/4/6 0006.
 */
public class EditPhoneVpAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<PhotoView> arrayList;

    public EditPhoneVpAdapter(Context context, ArrayList<PhotoView> arrayList) {
        this.context = context;

        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        View imageLayout = LayoutInflater.from(context).inflate(R.layout.item_view_pager, container, false);
//        PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.iv_view_pager_image);
//        Glide.with(context).load("file://"+arrayList.get(position))
//
//                .into(imageView);
//        container.addView(imageLayout);
//        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
           // mAttacher.update();

        container.addView(arrayList.get(position));
        return arrayList.get(position);
//        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//        container.removeViewAt(position);
        container.removeView(arrayList.get(position));

        //super.destroyItem(container, position, object);
    }
}
