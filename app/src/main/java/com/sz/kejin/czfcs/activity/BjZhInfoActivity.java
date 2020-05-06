package com.sz.kejin.czfcs.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.ImageGridViewAdapter;
import com.sz.kejin.czfcs.adapter.ImagesPagerAdapter;
import com.sz.kejin.czfcs.adapter.SpinnerAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.FwhcXxBeans;
import com.sz.kejin.czfcs.bean.FwhcZhXxBeans;
import com.sz.kejin.czfcs.bean.ImageInfo;
import com.sz.kejin.czfcs.bean.ImageUploadBean;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.UserInfoBySfzBean;
import com.sz.kejin.czfcs.bean.YcInfoBeans;
import com.sz.kejin.czfcs.bean.ZiDianBean;
import com.sz.kejin.czfcs.constant.AlertViewDataConstants;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.IntentConstants;
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
import com.sz.kejin.czfcs.view.MyGridView;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;
import okhttp3.Call;
import okhttp3.Response;

import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.mzStrs;
import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.xbStrs;

public class BjZhInfoActivity extends BasicActivity {

    private static final String TAG = "BjZhInfoActivity";


    private TextView tv_back;
    private ImageView iv_back;
    private MyGridView gvPcrPto1,gvPcrPto2;



    private TextView btn_tj;



    //图片相关
    private ArrayList<String> listPhoto1 = new ArrayList<>();
    private ArrayList<String> listPhoto2 = new ArrayList<>();


    private ArrayList<String> listPhotoBDAdress1 = new ArrayList<>();
    private ArrayList<String> listPhotoBDAdress2 = new ArrayList<>();


    private ArrayList<String> oldAddress1 = new ArrayList<>();
    private ArrayList<String> oldAddress2 = new ArrayList<>();


    private ArrayList<String> photoAddress1 = new ArrayList<>();
    private ArrayList<String> photoAddress2 = new ArrayList<>();


    private ImageGridViewAdapter gridViewAdapter1;
    private ImageGridViewAdapter gridViewAdapter2;

    private int type = 1;// 1 房间照片  2 车库照片  3 房产证照片



    private FwhcZhXxBeans beans;



    private EditText et_xm,et_sfzh,et_sjh,et_gzdw,et_bz;
    private TextView et_xb,et_mz,et_zlsjq,et_zlsjz,et_czfs,et_sf,et_c,et_x;
    private LinearLayout layout_zlsjq,layout_zlsjz,layout_czfs,layout_xb,layout_mz,layout_s,layout_c,layout_x;

    private int zhid;


    private RelativeLayout rLayout2;
    private LinearLayout layout2;


    private Spinner sp1,sp2,sp3;
    private SpinnerAdapter adapter1,adapter2,adapter3;
    private ArrayList<YcInfoBeans> datas1,datas2,datas3 ;
    private String strS,strC,strX;

    private String oldS,oldC,oldX;


    private String[] czfsStrs = new String[]{};
    private String czfs ="";


    private String[] sfStrs;
    private String[] cfStrs;
    private String[] xfStrs ;







    @Override
    protected int getLayoutId() {
        return R.layout.activity_bjzh_info;
    }

    @Override
    protected void initView() {
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);

        et_xm = findViewById(R.id.et_bjzh_czrxm);
        et_xb = findViewById(R.id.et_bjzh_xb);
        et_mz = findViewById(R.id.et_bjzh_mz);
        et_sfzh = findViewById(R.id.et_bjzh_sfzh);
        et_sjh = findViewById(R.id.et_bjzh_sjh);
        et_gzdw = findViewById(R.id.et_bjzh_gzdw);
        et_zlsjq = findViewById(R.id.et_bjzh_zlsjq);
        et_zlsjz = findViewById(R.id.et_bjzh_zlsjz);
        et_czfs = findViewById(R.id.et_bjzh_czfs);
        et_bz = findViewById(R.id.et_bz);

        et_sf = findViewById(R.id.et_bjzh_s);
        et_c = findViewById(R.id.et_bjzh_c);
        et_x = findViewById(R.id.et_bjzh_x);
        layout_s = findViewById(R.id.layout_s);
        layout_c = findViewById(R.id.layout_c);
        layout_x = findViewById(R.id.layout_x);

        layout_zlsjq = findViewById(R.id.layout_zlsjq);
        layout_zlsjz = findViewById(R.id.layout_zlsjz);
        layout_czfs = findViewById(R.id.layout_czfs);
        layout_xb = findViewById(R.id.layout_bjzh_xb);
        layout_mz = findViewById(R.id.layout_mz);


        btn_tj = findViewById(R.id.tv_tj);

        rLayout2 = findViewById(R.id.rLayout2);
        layout2 = findViewById(R.id.lLayout2);


        gvPcrPto1 = findViewById(R.id.gv_pcr_pto1);
        gvPcrPto2 = findViewById(R.id.gv_pcr_pto2);

        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        sp3 = findViewById(R.id.sp3);

    }

    private String userType = "租户";
    private String srcType = IntentConstants.ADD;
    private String fjId = "";

    private MainFyXxListBeans fjDetailBeans;

    @Override
    protected void initData(Bundle savedInstanceState) {
        fjId = getIntent().getIntExtra(IntentConstants.FJ_DATA, 0) + "";
        srcType = getIntent().getStringExtra(IntentConstants.EDIT_OR_ADD_TYPE);

        getFjInfoDetail();

        if (IntentConstants.ADD.equals(srcType)) {
            if ("家庭成员".equals(userType)) {
                setTitle("添加家庭成员");
            } else if ("租户".equals(userType)) {
                setTitle("添加租户");
                layout2.setVisibility(View.VISIBLE);
                rLayout2.setVisibility(View.VISIBLE);
            }
        } else if ("edit".equals(srcType)) {

            zhid = getIntent().getIntExtra(IntentConstants.ZH_DATA, 0);

            if ("家庭成员".equals(srcType)) {
                setTitle("编辑家庭成员信息");
            } else if ("租户".equals(userType)) {
                setTitle("编辑租户信息");
                layout2.setVisibility(View.VISIBLE);
                rLayout2.setVisibility(View.VISIBLE);
            } else if ("业主".equals(srcType)) {
                setTitle("编辑业主信息");
            }
            getZhInfoById();
        }


        getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
            @Override
            public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                super.onPermissionGranted(grantedPermissions);
            }
        },PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE, PermissionHelper.Permission.CAMERA);


        if (gridViewAdapter1 == null) {
            gridViewAdapter1 = new ImageGridViewAdapter(this, listPhotoBDAdress1);
        }
        gvPcrPto1.setAdapter(gridViewAdapter1);



        if (gridViewAdapter2 == null) {
            gridViewAdapter2 = new ImageGridViewAdapter(this, listPhotoBDAdress2);
        }
        gvPcrPto2.setAdapter(gridViewAdapter2);


        datas1 = new ArrayList<>();
        datas2 = new ArrayList<>();
        datas3 = new ArrayList<>();


        adapter1 = new SpinnerAdapter(BjZhInfoActivity.this,datas1,0);
        sp1.setAdapter(adapter1);

        adapter2 = new SpinnerAdapter(BjZhInfoActivity.this,datas2,1);
        sp2.setAdapter(adapter2);


        adapter3 = new SpinnerAdapter(BjZhInfoActivity.this,datas3,2);
        sp3.setAdapter(adapter3);

        getSfList();
        getCzfsList();
    }

    private void getFjInfoDetail(){
        HashMap<String, String> params = new HashMap<>();
        params.put("roomid", fjId + "");

        String id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String kind = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,"");
        params.put("scrid", id);
        params.put("kind", kind);

        OkHttpHelper.enqueue(OkHttpHelper.GET_FY_INFO_DETAIL, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {

                    String json = response.body().string();
                    final BaseBean<ArrayList<MainFyXxListBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<MainFyXxListBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (dataResult.getData().size() > 0) {
                                    fjDetailBeans = dataResult.getData().get(0);
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



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
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
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
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
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
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



    private ArrayList<String> czfsTexts = new ArrayList<>();
    private ArrayList<String> czfsVals = new ArrayList<>();

    private void getCzfsList(){
        HashMap<String, String> params = new HashMap<>();


        OkHttpHelper.enqueue(OkHttpHelper.GET_CZ_LX_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<ZiDianBean>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<ZiDianBean>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (dataResult.getData() != null) {
                                    for (int i = 0; i <dataResult.getData().size() ; i++) {
                                        czfsTexts.add(dataResult.getData().get(i).getText());
                                        czfsVals.add(dataResult.getData().get(i).getVal());
                                    }

                                    czfsStrs = czfsTexts.toArray(czfsStrs);

                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }




    public   void  getZhInfoById(){
        HashMap<String, String> params = new HashMap<>();
        params.put("zhid",zhid + "");



        OkHttpHelper.enqueue(OkHttpHelper.GET_FWHC_ZH_INFO_BY_ID, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<FwhcZhXxBeans>>  dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<FwhcZhXxBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult != null && dataResult.getCode() == 1 ) {
                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //租户信息
                                beans = dataResult.getData().get(0);
                                if (beans != null) {
                                    et_xm.setText(beans.getXm());
                                    et_xb.setText(beans.getXb());
                                    et_mz.setText(beans.getMz());
                                    et_sfzh.setText(beans.getSfzh());
                                    et_sjh.setText(beans.getLxdh());
                                    et_gzdw.setText(beans.getGzdw());
                                    et_gzdw.setText(beans.getGzdw());
                                    et_zlsjq.setText(beans.getStartTime());
                                    et_zlsjz.setText(beans.getEndTime());
                                    et_czfs.setText(beans.getCzfs());
                                    et_bz.setText(beans.getBz());

                                    et_sf.setText(beans.getHdj_s());
                                    et_c.setText(beans.getHdj_city());
                                    et_x.setText(beans.getHdj_x());


                                    oldS = beans.getHdj_s();
                                    oldC = beans.getHdj_city();
                                    oldX = beans.getHdj_x();

                                    oldAddress1.add(beans.getZlhtzpPath());
                                    oldAddress2.add(beans.getBrzpPath());

                                    if (!TextUtils.isEmpty(beans.getZlhtzpPath())) {
                                        String[] str1 = beans.getZlhtzpPath().split(",");
                                        for (int i = 0; i < str1.length; i++) {
                                            listPhotoBDAdress1.add(str1[i]);
                                        }
                                    }


                                    if (!TextUtils.isEmpty(beans.getBrzpPath())) {

                                        String[] str2 = beans.getBrzpPath().split(",");

                                        for (int i = 0; i < str2.length; i++) {
                                            listPhotoBDAdress2.add(str2[i]);
                                        }
                                    }




                                    gridViewAdapter1.notifyDataSetChanged();
                                    gridViewAdapter2.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
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

        layout_zlsjq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
//                startDate.set(2013,1,1);
                Calendar endDate = Calendar.getInstance();
//                endDate.set(2020,1,1);
                Calendar defaultDate = Calendar.getInstance();
//                defaultDate.set(1990,1,1);

                //正确设置方式 原因：注意事项有说明
                startDate.set(2010,1,1);
                endDate.set(2035,12,31);
                defaultDate.set(2020,1,1);
                TimePickerView pvTime = new TimePickerBuilder(BjZhInfoActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String birthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        et_zlsjq.setText(birthday);
                    }
                }).setCancelText("取消").setSubmitText("确定")
                        .setDate(defaultDate)
                        .setRangDate(startDate, endDate)
                        .build();
                pvTime.show();
            }
        });


        layout_zlsjz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
//                startDate.set(2013,1,1);
                Calendar endDate = Calendar.getInstance();
//                endDate.set(2020,1,1);
                Calendar defaultDate = Calendar.getInstance();
//                defaultDate.set(1990,1,1);

                //正确设置方式 原因：注意事项有说明
                startDate.set(2010,1,1);
                endDate.set(2035,12,31);
                defaultDate.set(2020,1,1);
                TimePickerView pvTime = new TimePickerBuilder(BjZhInfoActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String birthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        et_zlsjz.setText(birthday);
                    }
                }).setCancelText("取消").setSubmitText("确定")
                        .setDate(defaultDate)
                        .setRangDate(startDate, endDate)
                        .build();
                pvTime.show();
            }
        });




        layout_xb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择性别", null, null,null, xbStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_xb.setText(xbStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });

        layout_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择", null, null,null, sfStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
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
                new AlertView("请选择", null, null,null, cfStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
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
                new AlertView("请选择", null, null,null, xfStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_x.setText(xfStrs[position]);
                    }
                }).setCancelable(true).show();
            }
        });


        layout_mz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择民族", null, null,null, mzStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_mz.setText(mzStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });







        layout_czfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择出租方式", null, null,null, czfsStrs,  BjZhInfoActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_czfs.setText(czfsStrs[position]);
                        czfs = czfsVals.get(position);

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

        gridViewAdapter1.addItem(new ImageGridViewAdapter.AddItem() {
            @Override
            public void add(int postion) {
                type = 1;
                startCamera();
            }
        });

        gridViewAdapter1.deleteItem(new ImageGridViewAdapter.DeleteItem() {
            @Override
            public void delete(int postion) {
                for (Iterator<MediaBean> iterator = selected1.iterator(); iterator.hasNext(); ) {
                    MediaBean bean = iterator.next();
                    if (listPhotoBDAdress1.get(postion).equals(bean.getOriginalPath())) {
                        iterator.remove();
                    }
                }
                for (Iterator<String> iterator = photoAddress1.iterator(); iterator.hasNext();) {
                    String path = iterator.next();
                    if (listPhotoBDAdress1.get(postion).equals(path)) {
                        iterator.remove();
                    }
                }

                listPhotoBDAdress1.remove(postion);
//                listPhoto1.remove(postion);
                gridViewAdapter1.notifyDataSetChanged();
            }
        });

        gridViewAdapter1.choseItem(new ImageGridViewAdapter.ChoeseItem() {
            @Override
            public void chose(int postion) {
                Intent intent = new Intent(BjZhInfoActivity.this, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("list", listPhotoBDAdress1);//图片的集合
                startActivity(intent);
            }
        });



        gridViewAdapter2.addItem(new ImageGridViewAdapter.AddItem() {
            @Override
            public void add(int postion) {
                type = 2;
                startCamera();
            }
        });

        gridViewAdapter2.deleteItem(new ImageGridViewAdapter.DeleteItem() {
            @Override
            public void delete(int postion) {
                for (Iterator<MediaBean> iterator = selected2.iterator(); iterator.hasNext(); ) {
                    MediaBean bean = iterator.next();
                    if (listPhotoBDAdress2.get(postion).equals(bean.getOriginalPath())) {
                        iterator.remove();
                    }
                }

                for (Iterator<String> iterator = photoAddress2.iterator(); iterator.hasNext();) {
                    String path = iterator.next();
                    if (listPhotoBDAdress2.get(postion).equals(path)) {
                        iterator.remove();
                    }
                }

                listPhotoBDAdress2.remove(postion);
//                listPhoto2.remove(postion);
                gridViewAdapter2.notifyDataSetChanged();
            }
        });

        gridViewAdapter2.choseItem(new ImageGridViewAdapter.ChoeseItem() {
            @Override
            public void chose(int postion) {
                Intent intent = new Intent(BjZhInfoActivity.this, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("list", listPhotoBDAdress2);//图片的集合
                startActivity(intent);
            }
        });




        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageView imageView = new ImageView(BjZhInfoActivity.this);
//                imageView.setImageResource(R.drawable.suprise);
//                hud = KProgressHUD.create(BjZhInfoActivity.this)
//                        .setCustomView(imageView)
//                        .setLabel("正在保存...")
//                        .setDimAmount(0.5f)
//                        .setCancellable(true)
//                        .show();
//                DialogUtils.scheduleDismiss(hud,3000);
                startThreadCompressAndUpload();
            }
        });
    }

//    private KProgressHUD hud;

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
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }

    private void uploadYhInfo(){

        String loginid = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String name = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        HashMap<String, String> params = new HashMap<>();
        params.put("action", srcType);
        params.put("roomid", fjId + "");

        if (IntentConstants.ADD.equals(srcType)) {
            if ("家庭成员".equals(userType)) {
                params.put("ly", "cy");
            } else if ("租户".equals(userType)) {
                params.put("ly", "zh");
            }
            params.put("zhid", "");
        } else if (IntentConstants.EDIT.equals(srcType)) {//编辑ly不传
            params.put("ly", "");
            params.put("zhid", beans.getId() + "");
        }
        params.put("loginid", loginid);
        params.put("realname", name);
        if (TextUtils.isEmpty(et_xm.getText().toString())) {
            ToastUtil.shortToast(BjZhInfoActivity.this,"请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(et_sfzh.getText().toString())) {
            ToastUtil.shortToast(BjZhInfoActivity.this,"请输入身份证号");
            return;
        }

        if (!new IDCardValidator().isValidatedAllIdCard(et_sfzh.getText().toString())) {
            ToastUtil.shortToast(BjZhInfoActivity.this,"请输入有效的身份证");
            return;
        }


        if (TextUtils.isEmpty(et_mz.getText().toString())) {
            ToastUtil.shortToast(BjZhInfoActivity.this,"请输入民族");
            return;
        }


        if (listPhoto1.size() <= 0) {
            ToastUtil.shortToast(BjZhInfoActivity.this,"请至少拍一张照片");
            return;
        }

        if (!TextUtils.isEmpty(et_sjh.getText().toString().trim())) {
            if (!PhoneUtils.isPhoneNumber(et_sjh.getText().toString().trim())) {
                ToastUtil.shortToast(BjZhInfoActivity.this,"请输入正确的手机号码");
                return;
            }
        }

        params.put("xm", et_xm.getText().toString());
        params.put("xb", et_xb.getText().toString());
        params.put("mz", et_mz.getText().toString());
        params.put("sfzh", et_sfzh.getText().toString());
        params.put("lxdh", et_sjh.getText().toString());

        if (IntentConstants.ADD.equals(srcType)) {
            params.put("csqLoginid", fjDetailBeans.getCsqLoginid());
            params.put("xqbh", fjDetailBeans.getXqbh());
            params.put("ldh", fjDetailBeans.getLdh() + "");
            params.put("sh", fjDetailBeans.getSh() + "");
        } else if (IntentConstants.EDIT.equals(srcType)) {//编辑ly不传
            params.put("csqLoginid", beans.getCsqLoginid());
            params.put("xqbh", beans.getXqbh());
            params.put("ldh", beans.getLdh() + "");
            params.put("sh", beans.getSh() + "");
        }

        params.put("gzdw", et_gzdw.getText().toString());

        params.put("hjd_s", et_sf.getText().toString().trim());
        params.put("hjd_c", et_c.getText().toString().trim());
        params.put("hjd_x", et_x.getText().toString().trim());

        params.put("czfs",czfs);
        params.put("bz",et_bz.getText().toString());





        String path = "";
        for (int i = 0; i < listPhoto1.size(); i++) {
            if (i == listPhoto1.size() - 1) {
                path += listPhoto1.get(i);
            } else {
                path += listPhoto1.get(i) + "|";
            }
        }
        if ("租户".equals(userType)) {
            params.put("zlhtzpPath", path);
        } else {
            params.put("zlhtzpPath", "");
        }



        params.put("startTime", et_zlsjq.getText().toString());
        params.put("endTime", et_zlsjz.getText().toString());


        String path1 = "";
        for (int i = 0; i < listPhoto2.size(); i++) {
            if (i == listPhoto2.size() - 1) {
                path1 += listPhoto2.get(i);
            } else {
                path1 += listPhoto2.get(i) + "|";
            }
        }

        params.put("brzpPath", path1);




        OkHttpHelper.enqueue(OkHttpHelper.EDIT_ZH_INFO, params, new OkHttpHelper.Callback() {
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
//                            if (hud != null && hud.isShowing()) {
//                                hud.dismiss();
//                            }

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
                                }
                                finish();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(BjZhInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(BjZhInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1101:
                startCamera();
                break;
        }

    }



    private ArrayList<MediaBean> selected1 = new ArrayList<>();
    private ArrayList<MediaBean> selected2 = new ArrayList<>();


    private void startCamera(){
        ActionSheet actionSheet = new ActionSheet(BjZhInfoActivity.this);
        actionSheet.setCancelButtonTitle("取消");
        actionSheet.addItems("拍照", "打开相册");
        actionSheet.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(ActionSheet actionSheet, int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        if (PermissionCheckUtils.checkCameraPermission(BjZhInfoActivity.this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)) {
                            RxGalleryFinalApi.openZKCamera(BjZhInfoActivity.this);
                        }
                        break;
                    case 1:
                        switch (type) {
                            case 1:
                                RxGalleryFinal.with(BjZhInfoActivity.this).image().multiple().maxSize(GetImageUtils.UPLOAD_MAX_NUM - photoAddress1.size() ).imageLoader(ImageLoaderType.GLIDE).selected(selected1)
                                        .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                                            @Override
                                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                                List<MediaBean> result = imageMultipleResultEvent.getResult();
                                                selected1.clear();
//                                                listPhotoBDAdress1.clear();
                                                selected1.addAll(result);
                                                if (result != null && result.size() > 0) {
                                                    for (int i = 0; i < result.size(); i++) {
                                                        if (!listPhotoBDAdress1.contains(result.get(i).getOriginalPath())) {
                                                            listPhotoBDAdress1.add(result.get(i).getOriginalPath());
                                                        }
                                                    }
                                                }
//                                                listPhotoBDAdress1.addAll(photoAddress1);
                                                gridViewAdapter1.notifyDataSetChanged();
                                            }
                                        }).openGallery();
                                break;

                            case 2:
                                RxGalleryFinal.with(BjZhInfoActivity.this).image().multiple().maxSize(GetImageUtils.UPLOAD_MAX_NUM ).imageLoader(ImageLoaderType.GLIDE).selected(selected2)
                                        .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                                            @Override
                                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                                List<MediaBean> result = imageMultipleResultEvent.getResult();
                                                selected2.clear();
//                                                listPhotoBDAdress2.clear();
                                                selected2.addAll(result);
                                                if (result != null && result.size() > 0) {
                                                    for (int i = 0; i < result.size(); i++) {
                                                        if (!listPhotoBDAdress2.contains(result.get(i).getOriginalPath())) {
                                                            listPhotoBDAdress2.add(result.get(i).getOriginalPath());
                                                        }
                                                    }
                                                }
//                                                listPhotoBDAdress2.addAll(photoAddress2);
                                                gridViewAdapter2.notifyDataSetChanged();
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





    private KProgressHUD kProgressHUD ;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            switch (type) {
                case 1:
                    listPhotoBDAdress1.add(RxGalleryFinalApi.fileImagePath.getPath());
                    photoAddress1.add(RxGalleryFinalApi.fileImagePath.getPath());
                    gridViewAdapter1.notifyDataSetChanged();
                    break;

                case 2:
                    listPhotoBDAdress2.add(RxGalleryFinalApi.fileImagePath.getPath());
                    photoAddress2.add(RxGalleryFinalApi.fileImagePath.getPath());
                    gridViewAdapter2.notifyDataSetChanged();
                    break;
            }
        }
    }

    private ImageHandleThread imageHandleThread = new ImageHandleThread();
    private int index,index2;


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
            } else {
                if (index2 < listPhotoBDAdress2.size()) {
                    String fileStr = listPhotoBDAdress2.get(index2);
                    Message message = new Message();
                    message.what = 5;
                    message.obj = fileStr;
                    message.arg1 = 2;
                    handler.sendMessage(message);
                } else {
                    uploadYhInfo();
                }
            }
        }
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
                case 2:
                    index2++;
                    listPhoto2.add(imgPath);
                    break;
            }
            startThreadCompressAndUpload();
        } else {
            CompressBtimapUtil.compressBitmap(bitmap, imgPath, new CompressBtimapUtil.OnImgPressListener() {
                @Override
                public void imgPressListener(File file) {
                    if (file == null) {
                        ToastUtil.shortToast(BjZhInfoActivity.this,"图片压缩失败,请重试");
                        return;
                    }

                    HashMap<String, File> map = new HashMap<>();
                    HashMap<String, String> hashMap = new HashMap<>();
                    map.put("file", file);
                    OkhttpUtils.postFile(BjZhInfoActivity.this, OkHttpHelper.UPLOAD_URL, ImageUploadBean.class, map,
                            new OkhttpUtils.OnCallBackListener() {
                                @Override
                                public void onSucess(Object object) {
                                    ImageUploadBean bean = (ImageUploadBean) object;
                                    String path = bean.getInfo().get(0).getPath();

                                    switch (type) {
                                        case 1:
                                            index++;
                                            listPhoto1.add(path);
                                            break;
                                        case 2:
                                            index2++;
                                            listPhoto2.add(path);
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
