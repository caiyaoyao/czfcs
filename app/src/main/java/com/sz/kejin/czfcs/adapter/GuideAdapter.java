package com.sz.kejin.czfcs.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class GuideAdapter extends PagerAdapter {

    private ArrayList<ImageView> mImageViewList;
    private ViewPager mViewPager;

    public GuideAdapter(ArrayList<ImageView> mImageViewList, ViewPager mViewPager) {
        this.mImageViewList = mImageViewList;
        this.mViewPager = mViewPager;
    }

    @Override
    public int getCount() {
        return mImageViewList.size() ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中
        ImageView iv = mImageViewList.get(position % mImageViewList.size());
        mViewPager.addView(iv);
        // 把当前添加ImageView返回回去.
        return iv;
    }
    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 把ImageView从ViewPager中移除掉
        mViewPager.removeView(mImageViewList.get(position % mImageViewList.size()));

    }
}
