package com.sz.kejin.czfcs.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.SpinnerAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.ImageUploadBean;
import com.sz.kejin.czfcs.bean.UserInfoBean;
import com.sz.kejin.czfcs.bean.UserInfoBySfzBean;
import com.sz.kejin.czfcs.bean.YcInfoBeans;
import com.sz.kejin.czfcs.bean.YzmBeans;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.sfz.IDCardValidator;
import com.sz.kejin.czfcs.utils.CompressBtimapUtil;
import com.sz.kejin.czfcs.utils.GetImageUtils;
import com.sz.kejin.czfcs.utils.OkhttpUtils;
import com.sz.kejin.czfcs.utils.PhoneUtils;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.ActionSheet;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.mzStrs;
import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.xbStrs;

public class XiugaiXxActivity extends BasicActivity {
    private static final String TAG = "XiugaiXxActivity";
    private ImageView iv_back;

    private EditText et_sjh,et_yzm,et_xm,et_sfzh,et_gzdw;
    private TextView et_xb,et_mz,et_sf,et_c,et_x;
    private LinearLayout layout_mz,layout_xb,layout_s,layout_c,layout_x;
    private TextView tv_yzm,tv_tj;

    private Spinner sp1,sp2,sp3;
    private SpinnerAdapter adapter1,adapter2,adapter3;
    private ArrayList<YcInfoBeans> datas1,datas2,datas3 ;
    private String strS,strC,strX;

    private String oldS,oldC,oldX;

    private int type = 1;// 1 更换头像
    private CircleImageView txImg;

    private String user_type = Constants.USER_TYPE_YK;

    //图片相关
    private ArrayList<String> listPhoto1 = new ArrayList<>();


    private ArrayList<String> listPhotoBDAdress1 = new ArrayList<>();

    private String id = "";
    private String oldSjh = "";


    private String[] sfStrs ;
    private String[] cfStrs ;
    private String[] xfStrs ;


    private void getGrxx(){

        HashMap<String, String> params = new HashMap<>();

        params.put("kind", user_type);
        params.put("id", id);


        OkHttpHelper.enqueue(OkHttpHelper.GET_GR_XX, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<UserInfoBean>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<UserInfoBean>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (dataResult.getData() != null && dataResult.getData().size() > 0) {//更新ui
                                    UserInfoBean userInfoBean = dataResult.getData().get(0);

                                    oldSjh = userInfoBean.getLxdh();

                                    et_sjh.setText(userInfoBean.getLxdh());
                                    et_xm.setText(userInfoBean.getXm());
                                    et_sfzh.setText(userInfoBean.getSfzh());


                                    et_sf.setText(userInfoBean.getHdj_s());
                                    et_c.setText(userInfoBean.getHdj_city());
                                    et_x.setText(userInfoBean.getHdj_x());

                                    oldS = userInfoBean.getHdj_s();
                                    oldC = userInfoBean.getHdj_city();
                                    oldX = userInfoBean.getHdj_x();

                                    et_gzdw.setText(userInfoBean.getGzdw());
                                    et_mz.setText(userInfoBean.getMz());

                                    getSfList();

                                    Glide.with(XiugaiXxActivity.this).load(OkHttpHelper.ROOT + userInfoBean.getZpPath())
                                            .override(60,60)
                                            .error(R.mipmap.hl_new_card_no_no)
                                            .into(txImg);
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_xiugai_xx;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);
        et_sjh = findViewById(R.id.et_sjh);
        et_yzm = findViewById(R.id.et_yzm);
        et_xm = findViewById(R.id.et_xm);
        et_sfzh = findViewById(R.id.et_sfzh);
        et_xb = findViewById(R.id.et_xb);
        et_mz = findViewById(R.id.et_mz);
        et_gzdw = findViewById(R.id.et_gzdw);

        et_sf = findViewById(R.id.et_bjzh_s);
        et_c = findViewById(R.id.et_bjzh_c);
        et_x = findViewById(R.id.et_bjzh_x);
        layout_s = findViewById(R.id.layout_s);
        layout_c = findViewById(R.id.layout_c);
        layout_x = findViewById(R.id.layout_x);


        layout_mz = findViewById(R.id.layout_mz);
        layout_xb = findViewById(R.id.layout_xb);

        tv_yzm = findViewById(R.id.tv_get_yzm);

        tv_tj = findViewById(R.id.tv_tj);
        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        sp3 = findViewById(R.id.sp3);

        txImg = findViewById(R.id.civ_grtx);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("修改信息");

        getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
            @Override
            public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                super.onPermissionGranted(grantedPermissions);
            }
        },PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE, PermissionHelper.Permission.CAMERA);


        datas1 = new ArrayList<>();
        datas2 = new ArrayList<>();
        datas3 = new ArrayList<>();


        adapter1 = new SpinnerAdapter(XiugaiXxActivity.this,datas1,0);
        sp1.setAdapter(adapter1);

        adapter2 = new SpinnerAdapter(XiugaiXxActivity.this,datas2,1);
        sp2.setAdapter(adapter2);


        adapter3 = new SpinnerAdapter(XiugaiXxActivity.this,datas3,2);
        sp3.setAdapter(adapter3);


        id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        user_type = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,Constants.USER_TYPE_YK);
        getGrxx();


    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getyzm(et_sjh.getText().toString());
            }
        });

        txImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type =1;
                index = 0;
                listPhotoBDAdress1.clear();
                listPhoto1.clear();
                startCamera();
            }
        });

        layout_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择", null, null,null, sfStrs,  XiugaiXxActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_sf.setText(sfStrs[position]);
                        getCityList(et_sf.getText().toString());
                        et_c.setText("");
                        et_x.setText("");
                    }
                }).setCancelable(true).show();
            }
        });

        layout_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择", null, null,null, cfStrs,  XiugaiXxActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_c.setText(cfStrs[position]);
                        getQxList(et_c.getText().toString());
                        et_x.setText("");
                    }
                }).setCancelable(true).show();
            }
        });


        layout_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择", null, null,null, xfStrs,  XiugaiXxActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_x.setText(xfStrs[position]);
                    }
                }).setCancelable(true).show();
            }
        });



        layout_xb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择性别", null, null,null, xbStrs,  XiugaiXxActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_xb.setText(xbStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });


        layout_mz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择民族", null, null,null, mzStrs,  XiugaiXxActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_mz.setText(mzStrs[position]);

                    }
                }).setCancelable(true).show();
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



        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strS = datas1.get(position).getSmc();
                if (!strS.equals(oldS)) {
                    oldC = "";
                }
                getCityList(strS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strC = datas2.get(position).getDjsmc();
                if (!strC.equals(oldC)) {
                    oldX = "";
                }
                getQxList(strC);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strX = datas3.get(position).getQxmc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadXgInfo();
            }
        });
    }

    private void uploadXgInfo(){


        if (!TextUtils.isEmpty(et_sjh.getText().toString().trim())) {
            if (!PhoneUtils.isPhoneNumber(et_sjh.getText().toString().trim())) {
                ToastUtil.shortToast(this,"请输入正确的手机号码");
                return;
            }
        }else{
            ToastUtil.shortToast(this,"请输入手机号码");
            return;
        }

        if (!et_sjh.getText().toString().equals(oldSjh) && TextUtils.isEmpty(et_yzm.getText().toString())) {
            ToastUtil.shortToast(this,"请输入验证码");
            return;
        }



        HashMap<String, String> params = new HashMap<>();

        params.put("kind", user_type);
        params.put("id", id);
        params.put("lxdh", et_sjh.getText().toString().trim());
        params.put("yzm", et_yzm.getText().toString().trim());
        params.put("xm", et_xm.getText().toString().trim());
        params.put("xb", et_xb.getText().toString().trim());
        params.put("mz", et_mz.getText().toString().trim());
        params.put("hjd_s", et_sf.getText().toString().trim());
        params.put("hjd_c", et_c.getText().toString().trim());
        params.put("hjd_x", et_x.getText().toString().trim());
        params.put("gzdw", et_gzdw.getText().toString().trim());

        OkHttpHelper.enqueue(OkHttpHelper.XG_GR_XX, params, new OkHttpHelper.Callback() {
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
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
                                }
                                SPUtils.getInstance(XiugaiXxActivity.this).put(SPConstants.USER_LXDH,et_sjh.getText().toString());
                                SPUtils.getInstance(XiugaiXxActivity.this).put(SPConstants.USER_XM,et_xm.getText().toString());
                                finish();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
//                        if (hud != null && hud.isShowing()) {
//                            hud.dismiss();
//                        }
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }


    private void getyzm(String mobile) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.shortToast(XiugaiXxActivity.this, "请输入手机号");
            return;
        }
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            ToastUtil.shortToast(XiugaiXxActivity.this, "请输入正确的手机号");
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
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
                                }


                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtil.shortToast(XiugaiXxActivity.this,errorMsg);
                        }
                    }
                });
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

    private void getSfList(){
        HashMap<String, String> params = new HashMap<>();
        OkHttpHelper.enqueue(OkHttpHelper.GET_SF_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<YcInfoBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<YcInfoBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                datas1.clear();
                                datas1.addAll(dataResult.getData());

                                ArrayList<String> datas = new ArrayList<>();
                                for (int i = 0; i < dataResult.getData().size(); i++) {
                                    datas.add(dataResult.getData().get(i).getSmc());
                                }
                                sfStrs = new String[]{};
                                sfStrs = datas.toArray(sfStrs);

                                if (!TextUtils.isEmpty(oldS)) {
                                    strS = oldS;
                                    for (int i = 0; i < datas1.size(); i++) {
                                        if (oldS.equals(datas1.get(i).getSmc())) {
                                            sp1.setSelection(i);
                                            break;
                                        }
                                    }
                                } else {
                                    strS = datas1.get(0).getSmc();
                                    sp1.setSelection(0);
                                }

                                getCityList(strS);
                                adapter1.notifyDataSetChanged();

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }


    private void getCityList(String str){
        HashMap<String, String> params = new HashMap<>();
        params.put("s", str);
        OkHttpHelper.enqueue(OkHttpHelper.GET_S_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<YcInfoBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<YcInfoBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                datas2.clear();
                                datas2.addAll(dataResult.getData());

                                ArrayList<String> datas = new ArrayList<>();
                                for (int i = 0; i < dataResult.getData().size(); i++) {
                                    datas.add(dataResult.getData().get(i).getDjsmc());
                                }
                                cfStrs = new String[]{};
                                cfStrs = datas.toArray(cfStrs);
                                if (!TextUtils.isEmpty(oldC)) {
                                    strC = oldC;
                                    for (int i = 0; i < datas2.size(); i++) {
                                        if (oldC.equals(datas2.get(i).getDjsmc())) {
                                            sp2.setSelection(i);
                                            break;
                                        }
                                    }
                                } else {
                                    strC = datas2.get(0).getDjsmc();
                                    sp2.setSelection(0);
                                }

                                getQxList(strC);
                                adapter2.notifyDataSetChanged();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private void getQxList(String str){
        HashMap<String, String> params = new HashMap<>();
        params.put("city", str);
        OkHttpHelper.enqueue(OkHttpHelper.GET_QX_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<YcInfoBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<YcInfoBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                datas3.clear();
                                datas3.addAll(dataResult.getData());


                                ArrayList<String> datas = new ArrayList<>();
                                for (int i = 0; i < dataResult.getData().size(); i++) {
                                    datas.add(dataResult.getData().get(i).getQxmc());
                                }
                                xfStrs = new String[]{};
                                xfStrs = datas.toArray(xfStrs);

                                if (!TextUtils.isEmpty(oldX)) {
                                    strX = oldX;
                                    for (int i = 0; i < datas3.size(); i++) {
                                        if (oldX.equals(datas3.get(i).getQxmc())) {
                                            sp3.setSelection(i);
                                            break;
                                        }
                                    }
                                } else {
                                    if (datas3.size() > 0) {
                                        strX = datas3.get(0).getQxmc();
                                        sp3.setSelection(0);
                                    }
                                }


                                adapter3.notifyDataSetChanged();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }


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
                                et_xb.setText(dataResult.getData().getSex());

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }


    private void startCamera(){
        ActionSheet actionSheet = new ActionSheet(XiugaiXxActivity.this);
        actionSheet.setCancelButtonTitle("取消");
        actionSheet.addItems("拍照", "打开相册");
        actionSheet.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(ActionSheet actionSheet, int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        if (PermissionCheckUtils.checkCameraPermission(XiugaiXxActivity.this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)) {
                            RxGalleryFinalApi.openZKCamera(XiugaiXxActivity.this);
                        }
                        break;
                    case 1:
                        switch (type) {
                            case 1:
                                RxGalleryFinal.with(XiugaiXxActivity.this).image().multiple().maxSize(1).imageLoader(ImageLoaderType.GLIDE)
                                        .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                                            @Override
                                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                                List<MediaBean> result = imageMultipleResultEvent.getResult();
                                                if (result != null && result.size() > 0) {
                                                    String photoImg = result.get(0).getOriginalPath();//更换头像
                                                    listPhotoBDAdress1.clear();
                                                    listPhotoBDAdress1.add(photoImg);
                                                    startThreadCompressAndUpload();
//                                                    Glide.with(GrInfoActivity.this).load(photoImg)
//                                                            .override(60,60)
//                                                            .into(circleImageView);
                                                }
                                            }
                                        }).openGallery();

                                break;

                        }
                        break;
                }
            }
        });
        actionSheet.setCancelableOnTouchMenuOutside(true);
        actionSheet.showMenu();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            switch (type) {
                case 1:
                    String img = RxGalleryFinalApi.fileImagePath.getPath();//更换头像
                    listPhotoBDAdress1.clear();
                    listPhotoBDAdress1.add(img);
                    startThreadCompressAndUpload();
                    break;
            }
        }
    }

    private int index;

    private ImageHandleThread imageHandleThread = new ImageHandleThread();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 5) {//压缩上传图片
                String path = (String) msg.obj;
                int type = msg.arg1;
                compressAndUploadImg(type, path);
            }

        }
    };


    //开启线程，一张一张上传图片
    class ImageHandleThread extends Thread {
        public void run()
        {
            if (index < listPhotoBDAdress1.size()) {
                String fileStr = listPhotoBDAdress1.get(index);
                Message message = new Message();
                message.what = 5;
                message.obj = fileStr;
                message.arg1 = 1;
                handler.sendMessage(message);
            } else {//修改头像
                Log.i(TAG, "run: ");
                updateTx();
            }
        }
    }

    private void updateTx(){
        HashMap<String, String> params = new HashMap<>();

        params.put("kind", user_type);
        params.put("id", id);

        params.put("zpPath", listPhoto1.get(0));

        OkHttpHelper.enqueue(OkHttpHelper.XG_GR_TX, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<String> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<String>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
                                }
                                String path = dataResult.getData();
                                Log.i(TAG, "run: 1111111"  + path);

                                Glide.with(XiugaiXxActivity.this).load(OkHttpHelper.ROOT + path)
                                        .override(60,60)
                                        .error(R.mipmap.hl_new_card_no_no)
                                        .into(txImg);
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(XiugaiXxActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(XiugaiXxActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private  void  startThreadCompressAndUpload(){

        imageHandleThread = new ImageHandleThread();
        imageHandleThread.start();
    }


    private void compressAndUploadImg(final  int type , final String imgPath){
//        isClickUpload = true;
        final Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        String str = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!imgPath.contains(str)) {
            switch (type) {
                case 1:
                    index++;
                    listPhoto1.add(imgPath);
                    break;
            }
            startThreadCompressAndUpload();
        } else {
            CompressBtimapUtil.compressBitmap(bitmap, imgPath, new CompressBtimapUtil.OnImgPressListener() {
                @Override
                public void imgPressListener(File file) {
                    if (file == null) {
                        ToastUtil.shortToast(XiugaiXxActivity.this,"图片压缩失败,请重试");
                        return;
                    }

                    HashMap<String, File> map = new HashMap<>();
                    HashMap<String, String> hashMap = new HashMap<>();
                    map.put("file", file);
                    OkhttpUtils.postFile(XiugaiXxActivity.this, OkHttpHelper.UPLOAD_URL, ImageUploadBean.class, map,
                            new OkhttpUtils.OnCallBackListener() {
                                @Override
                                public void onSucess(Object object) {
                                    ImageUploadBean bean = (ImageUploadBean) object;
                                    String path = bean.getInfo().get(0).getPath();

                                    Log.i(TAG, "onSucess: 111111" + path);

                                    switch (type) {
                                        case 1:
                                            index++;
                                            listPhoto1.add(path);
                                            break;
                                    }
                                    startThreadCompressAndUpload();

                                    Log.i(TAG, "onSucess: ");
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.i(TAG, "onError: ");
                                    startThreadCompressAndUpload();
                                }
                            }, hashMap);
                }
            });
        }


    }
}
