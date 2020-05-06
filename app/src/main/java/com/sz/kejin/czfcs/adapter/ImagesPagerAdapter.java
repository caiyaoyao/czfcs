package com.sz.kejin.czfcs.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ImagesPagerAdapter extends PagerAdapter {
    private List<SimpleDraweeView> simpleDraweeViewList;
    private ViewPager viewPager;
    private Context context;

    private SimpleDraweeView simpleDraweeView;

    public ImagesPagerAdapter(List<SimpleDraweeView> simpleDraweeViewList, ViewPager viewPager, Context context) {
        this.simpleDraweeViewList = simpleDraweeViewList;
        this.viewPager = viewPager;
        this.context = context;
    }

    @Override
    public int getCount() {
       //返回一个无限大的值，可以 无限循环
        if (simpleDraweeViewList.size() > 4) {
            return Integer.MAX_VALUE;
        } else {
            return simpleDraweeViewList.size();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(simpleDraweeViewList.get(position % simpleDraweeViewList.size()));
        //super.destroyItem(container, position, object);
    }

    //是否获取缓存
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    //实例化Item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        if (simpleDraweeViewList.size() < 3) {
            try {
//            simpleDraweeView = simpleDraweeViewList.get(position % simpleDraweeViewList.size());
                if (simpleDraweeViewList.get(position).getParent() == null) {
                    viewPager.addView(simpleDraweeViewList.get(position % simpleDraweeViewList.size()));
                } else {
                    ((ViewGroup) simpleDraweeViewList.get(position).getParent()).removeView(simpleDraweeViewList.get(position % simpleDraweeViewList.size()));
                    viewPager.addView(simpleDraweeViewList.get(position % simpleDraweeViewList.size()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return simpleDraweeViewList.get(position % simpleDraweeViewList.size());
        } else {

            simpleDraweeView = simpleDraweeViewList.get(position % simpleDraweeViewList.size());
            viewPager.addView(simpleDraweeView);
            return simpleDraweeView;
        }

    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
