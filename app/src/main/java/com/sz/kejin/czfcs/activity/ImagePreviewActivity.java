package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.EditPhoneVpAdapter;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.view.ViewPagerFixed;


import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePreviewActivity extends BasicActivity implements ViewPager.OnPageChangeListener{
    private static final String TAG = "ImagePreviewActivity";
    private TextView tv_back;
    private ImageView iv_back;
    private ViewPagerFixed vp;

    private TextView tvNum;
    private int count;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<PhotoView> ListPhone = new ArrayList<>();
    private PhotoViewAttacher mAttacher;
    private EditPhoneVpAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);
        vp = (ViewPagerFixed)findViewById(R.id.vp_edit);
        tvNum = (TextView)findViewById(R.id.tv_photos_number);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("图片预览");
        Intent intent = getIntent();
        ArrayList<String> list = intent.getStringArrayListExtra("list");
        arrayList.addAll(list);
        count = intent.getIntExtra("count", 0);
        setData();
    }

    private void setData() {

        for (int i = 0; i < arrayList.size(); i++) {
            PhotoView photoView = new PhotoView(this);
            String path =arrayList.get(i);

            if (path.contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {

            } else {
                if (path.contains("http")) {
                } else {
                    path = OkHttpHelper.ROOT + path;
                }
            }
            Glide.with(this).load(path).error(R.mipmap.hl_new_card_no_no).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(photoView);

            mAttacher = new PhotoViewAttacher(photoView);
            mAttacher.update();
            ListPhone.add(photoView);
        }

        tvNum.setText(count + 1 + "/" + ListPhone.size());
        adapter = new EditPhoneVpAdapter(ImagePreviewActivity.this, ListPhone);
        vp.setAdapter(adapter);
        vp.setCurrentItem(count);
    }

    @Override
    protected void initListener() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        count = position;
        tvNum.setText(count + 1 + "/" + ListPhone.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
