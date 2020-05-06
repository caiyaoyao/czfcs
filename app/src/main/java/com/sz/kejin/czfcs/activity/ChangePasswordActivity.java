package com.sz.kejin.czfcs.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;


import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by renjiezhou on 17/1/26.
 */
public class ChangePasswordActivity extends BasicActivity {
    private static final String TAG = "ChangePasswordActivity";
    private ImageView iv_back;
    private TextView tv_back;
    private EditText et_xmm,et_ymm,et_qrxmm;
    private Button btn_commit;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView() {
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);
        et_xmm = findViewById(R.id.tv_xmm);
        et_ymm = findViewById(R.id.et_ymm);
        et_qrxmm = findViewById(R.id.tv_qrxmm);
        et_xmm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_ymm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_qrxmm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btn_commit = findViewById(R.id.btn_mm_commit);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("修改密码");
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

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword(){
        if (TextUtils.isEmpty(et_ymm.getText().toString())) {
            ToastUtil.shortToast(ChangePasswordActivity.this,"请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(et_xmm.getText().toString())) {
            ToastUtil.shortToast(ChangePasswordActivity.this,"请输入新密码");
            return;
        }





        String xmm = et_xmm.getText().toString().trim();
        if (xmm.length() < 6 || xmm.length() > 12) {
            ToastUtil.shortToast(ChangePasswordActivity.this,"6-12位");
            return;
        }

        if (TextUtils.isEmpty(et_qrxmm.getText().toString())) {
            ToastUtil.shortToast(ChangePasswordActivity.this,"请输入新密码");
            return;
        }


        if (!et_xmm.getText().toString().equals(et_qrxmm.getText().toString())) {
            ToastUtil.shortToast(ChangePasswordActivity.this,"两次密码不同");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        String loginid = SPUtils.getInstance(this).getString(SPConstants.LOGINID);
        params.put("uid",loginid);
        params.put("oldpwd",et_ymm.getText().toString());
        params.put("newpwd",et_xmm.getText().toString());


        OkHttpHelper.enqueue(OkHttpHelper.CHANGE_PASSWORD, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean dataResult = new Gson().fromJson(json, new TypeToken<BaseBean>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult.getCode() == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(ChangePasswordActivity.this,"更改成功");
                                finish();
                            }
                        });



                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(ChangePasswordActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ChangePasswordActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }
}
