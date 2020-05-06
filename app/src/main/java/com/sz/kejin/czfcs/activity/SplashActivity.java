package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;


/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class SplashActivity extends BasicActivity {
    private static final String TAG = "SphlashActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.splash_activity_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },3000);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onBackPressed() {


    }
}
