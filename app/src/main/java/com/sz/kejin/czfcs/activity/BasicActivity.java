package com.sz.kejin.czfcs.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.IPermission;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.net.kejin.baselibrary.helper.StatusBarHelper;


public abstract  class BasicActivity extends AppCompatActivity implements IPermission {
    private IntentFilter networkIntentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    protected boolean isAppInBackground = false;
    private PermissionHelper mPermissionHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.fitStatusBar(this);
        StatusBarHelper.setStatusBarLightMode(this);
        setContentView(getLayoutId());
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        initView();
        initData(savedInstanceState);
        initListener();
        configureNetworkState();

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (isAppInBackground) {
            isAppInBackground = false;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
        }
        mPermissionHelper = null;
    }


    /**
     * 隐藏状态栏
     * <p>
     * 在setContentView前调用
     */
    protected void hideStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    protected void showStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    }

    private void configureNetworkState() {
        networkIntentFilter = new IntentFilter();
        networkIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, networkIntentFilter);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context, "当前网络可用", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "无网络,请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

    public void setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close01, R.anim.activity_close);
    }

    public void requestPermission(OnPermissionGrantListener listener, PermissionHelper.Permission... permissions) {
        getPermissionHelper().requestPermission(listener, permissions);
    }

    @Override
    public PermissionHelper getPermissionHelper() {
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        return mPermissionHelper;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handlePermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
