package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;


/**
 * Created by renjiezhou on 17/1/26.
 */
public class AboutUsActivity extends BasicActivity {
    private static final String TAG = "AboutUsActivity";

    private  TextView telTV;
    private ImageView iv_back;
    private TextView tv_back;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        telTV = (TextView) findViewById(R.id.tv_tel);
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("出租房超市");
    }

    @Override
    protected void initListener() {
        telTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
                    @Override
                    public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                        super.onPermissionGranted(grantedPermissions);
                        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ "051257303999"));
                        startActivity(intent);
                    }
                },PermissionHelper.Permission.CALL_PHONE);

            }
        });

//        netTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转网址
//                Uri uri = Uri.parse("http://www.kejin.net.cn");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });
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
    }
}
