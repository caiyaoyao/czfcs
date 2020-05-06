package com.sz.kejin.czfcs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.LoginBean;
import com.sz.kejin.czfcs.bean.UpdateAppBean;
import com.sz.kejin.czfcs.bean.UserInfoBean;
import com.sz.kejin.czfcs.bean.VersionBean;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.manager.UpdateManager;
import com.sz.kejin.czfcs.sfz.IDCardValidator;
import com.sz.kejin.czfcs.utils.PhoneUtils;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BasicActivity {
    private static final String TAG = "LoginActivity";
    private ImageView iv_back;
    private EditText et_sfzh,et_sjh;
    private TextView tv_tj,tv_register;
    private RadioButton rb1,rb2;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);
        et_sfzh = findViewById(R.id.et_sfzh);
        et_sjh = findViewById(R.id.et_sjh);
        tv_tj = findViewById(R.id.tv_tj);
        tv_register = findViewById(R.id.tv_login_register);

        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("登录");
        String userName = SPUtils.getInstance(this).getString(SPConstants.USER_SFZH,"");
        et_sfzh.setText(userName);


        String password = SPUtils.getInstance(this).getString(SPConstants.USER_LXDH,"");
        et_sjh.setText(password);
    }



    @Override
    protected void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

    }
    private  String type = Constants.USER_TYPE_YK;


    private void login(){


        if (TextUtils.isEmpty(et_sfzh.getText().toString())) {
            ToastUtil.shortToast(this,"请输入身份证号");
            return;
        }

        if (!new IDCardValidator().isValidatedAllIdCard(et_sfzh.getText().toString())) {
            ToastUtil.shortToast(this,"请输入有效的身份证");
            return;
        }


        if (!TextUtils.isEmpty(et_sjh.getText().toString().trim())) {
            if (!PhoneUtils.isPhoneNumber(et_sjh.getText().toString().trim())) {
                ToastUtil.shortToast(this,"请输入正确的手机号码");
                return;
            }
        }else{
            ToastUtil.shortToast(this,"请输入手机号码");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("sfzh", et_sfzh.getText().toString().trim());
        params.put("lxdh", et_sjh.getText().toString().trim());
        if (rb1.isChecked()) {
            type = Constants.USER_TYPE_FD;
        } else if (rb2.isChecked()){
            type = Constants.USER_TYPE_YK;
        }
        params.put("kind", type);

        OkHttpHelper.enqueue(OkHttpHelper.LOGIN, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<UserInfoBean>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<UserInfoBean>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.USER_SFZH,et_sfzh.getText().toString());
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.USER_LXDH,et_sjh.getText().toString());
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.USER_XM,dataResult.getData().get(0).getXm());
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.LOGINID,dataResult.getData().get(0).getId() + "");
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.IS_LOGIN,true);
                        SPUtils.getInstance(LoginActivity.this).put(SPConstants.USER_TYPE,type);
                        finish();
                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(LoginActivity.this, dataResult.getMsg());
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRequestFail(Call call, final String errorMsg) {
                Log.i(TAG, "onRequestFail: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtil.shortToast(LoginActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }



}
