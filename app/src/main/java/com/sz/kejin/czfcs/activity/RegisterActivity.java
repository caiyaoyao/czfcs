package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.LoginBean;
import com.sz.kejin.czfcs.bean.SqBeans;
import com.sz.kejin.czfcs.bean.UserInfoBean;
import com.sz.kejin.czfcs.bean.UserInfoBySfzBean;
import com.sz.kejin.czfcs.bean.YzmBeans;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.sfz.IDCardValidator;
import com.sz.kejin.czfcs.utils.PhoneUtils;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.databinding.adapters.TextViewBindingAdapter;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BasicActivity {
    private static final String TAG = "RegisterActivity";
    private ImageView iv_back;

    private EditText et_sfzh,et_sjh,et_yzm;
    private TextView tv_tj,tv_login,tv_yzm;

    private RadioButton rb1,rb2;

    private String csrq = "",dzS = "" ,dzC = "",dzX = "",xb = "";





    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);
        et_sfzh = findViewById(R.id.et_sfzh);
        et_sjh = findViewById(R.id.et_sjh);
        et_yzm = findViewById(R.id.et_register_yzm);
        tv_tj = findViewById(R.id.tv_tj);
        tv_login = findViewById(R.id.tv_register_login);
        tv_yzm = findViewById(R.id.tv_get_yzm);

        rb1 = findViewById(R.id.rb1);


        rb2 = findViewById(R.id.rb2);

    }



    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("注册");
    }



    @Override
    protected void initListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        tv_yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getyzm(et_sjh.getText().toString());
            }
        });

        et_sfzh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (new IDCardValidator().isValidatedAllIdCard(et_sfzh.getText().toString())) {
                    getSfInfoBysfz();
                }
            }
        });

        tv_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });



    }

    CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_yzm.setText(String.format("%s(%d秒)", "重新获取", millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            tv_yzm.setText("重新获取");
            tv_yzm.setEnabled(true);
        }
    };

    private void getSfInfoBysfz(){
        HashMap<String, String> params = new HashMap<>();
        params.put("sfzh", et_sfzh.getText().toString());

        OkHttpHelper.enqueue(OkHttpHelper.GET_SFZ_INFO, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<UserInfoBySfzBean> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<UserInfoBySfzBean>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                xb = dataResult.getData().getSex();
                                csrq = dataResult.getData().getBirthday();
                                dzS = dataResult.getData().getProvince();
                                dzC = dataResult.getData().getCity();
                                dzX = dataResult.getData().getCounty();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(RegisterActivity.this, dataResult.getMsg());
                                }
                            }
                        }
                    });

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
                            ToastUtil.shortToast(RegisterActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }


    private void getyzm(String mobile) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.shortToast(RegisterActivity.this, "请输入手机号");
            return;
        }
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            ToastUtil.shortToast(RegisterActivity.this, "请输入正确的手机号");
            return;
        }
        countDownTimer.start();
        tv_yzm.setEnabled(false);
        params.put("lxdh", String.valueOf(mobile));
        OkHttpHelper.enqueue(OkHttpHelper.GET_YZM, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean dataResult = new Gson().fromJson(json, new TypeToken<BaseBean>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult != null && dataResult.getCode() == 1) {

                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(RegisterActivity.this, dataResult.getMsg());
                                }


                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(RegisterActivity.this, dataResult.getMsg());
                                }
                            }

                            tv_yzm.setEnabled(true);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRequestFail(Call call, String errorMsg) {
                Log.i(TAG, "onRequestFail: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_yzm.setEnabled(true);
                    }
                });
            }
        });
    }


    private String type = Constants.USER_TYPE_YK;

    private void register(){

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

        if (TextUtils.isEmpty(et_yzm.getText().toString())) {
            ToastUtil.shortToast(this,"请输入验证码");
            return;
        }



        HashMap<String, String> params = new HashMap<>();

        params.put("sfzh", et_sfzh.getText().toString().trim());
        params.put("lxdh", et_sjh.getText().toString().trim());
        params.put("yzm", et_yzm.getText().toString().trim());

        if (rb1.isChecked()) {
            type = Constants.USER_TYPE_FD;
        } else if (rb2.isChecked()){
            type = Constants.USER_TYPE_YK;
        }
        params.put("type", type);
        params.put("xb", xb);
        params.put("birthday", csrq);
        params.put("hjd_s", dzS);
        params.put("hjd_c", dzC);
        params.put("hjd_x", dzX);


        OkHttpHelper.enqueue(OkHttpHelper.USER_REGISTER, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<UserInfoBean>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<UserInfoBean>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {

                                if (Constants.USER_TYPE_FD.equals(type)) {//房东账号需要审核

                                } else {
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.USER_TYPE,type);
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.USER_SFZH,et_sfzh.getText().toString());
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.USER_LXDH,et_sjh.getText().toString());
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.USER_XM,dataResult.getData().get(0).getXm());
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.LOGINID,dataResult.getData().get(0).getId() + "");
                                    SPUtils.getInstance(RegisterActivity.this).put(SPConstants.IS_LOGIN,true);
                                }
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(RegisterActivity.this, dataResult.getMsg());
                                }
                                finish();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(RegisterActivity.this, dataResult.getMsg());
                                }
                            }
                        }
                    });

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
                            ToastUtil.shortToast(RegisterActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }

}
