package com.sz.kejin.czfcs.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.FwhcAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.FwhcXxBeans;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.utils.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import okhttp3.Call;
import okhttp3.Response;

public class ZhListActivity extends BasicActivity {
    private static final String TAG = "ZhListActivity";

    private TextView tv_back;
    private ImageView iv_back;

    private FwhcXxBeans.FjxxBean mbeans;

    private TextView tv_sscsq, tv_xqmc,tv_ldh2,tv_sh2, tv_yzxm;



    private int id;


    private RecyclerView rv_fwhc;
    private FwhcAdapter adapter;
    private ArrayList<FwhcXxBeans.ZhxxBean> datas;


    private TextView tv_tjzh;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_zh_list;
    }

    @Override
    protected void initView() {
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);


        tv_sscsq = findViewById(R.id.et_fjxx_sscsq);
        tv_xqmc = findViewById(R.id.et_fjxx_ssxq);
        tv_ldh2 = findViewById(R.id.et_fjxx_ldh);
        tv_sh2 = findViewById(R.id.et_fjxx_sh);
        tv_yzxm = findViewById(R.id.et_fjxx_yzxm);


        rv_fwhc = findViewById(R.id.rv_fwhc);

        rv_fwhc.setLayoutManager(new SyLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv_fwhc.setHasFixedSize(true);
        rv_fwhc.setNestedScrollingEnabled(false);


        tv_tjzh = findViewById(R.id.tv_tjzh);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("租户列表");
        id = getIntent().getIntExtra(IntentConstants.FJ_DATA,0);

        datas = new ArrayList<>();
        mbeans = new FwhcXxBeans.FjxxBean();

        adapter = new FwhcAdapter(ZhListActivity.this, datas, mbeans);
        rv_fwhc.setAdapter(adapter);




    }

    @Override
    protected void onResume() {
        super.onResume();
        getFjInfoById();
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

        tv_tjzh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加租户
                Intent intent = new Intent(ZhListActivity.this, BjZhInfoActivity.class);
                intent.putExtra(IntentConstants.EDIT_OR_ADD_TYPE, IntentConstants.ADD);
                intent.putExtra(IntentConstants.FJ_DATA, id);
                startActivity(intent);
            }
        });



    }

    private KProgressHUD hud;


    public   void  getFjInfoById(){
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id + "");



        OkHttpHelper.enqueue(OkHttpHelper.GET_FWHC_INFO_BY_ID, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<FwhcXxBeans> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<FwhcXxBeans>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult != null && dataResult.getCode() == 1) {
                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //房间信息
                                FwhcXxBeans beans0 = dataResult.getData();
                                if (beans0 != null && beans0.getFjxx().size() > 0) {


                                    FwhcXxBeans.FjxxBean beans = beans0.getFjxx().get(0);
                                    mbeans = beans;
                                    adapter = new FwhcAdapter(ZhListActivity.this, datas, mbeans);
                                    rv_fwhc.setAdapter(adapter);
                                    tv_ldh2.setText(beans.getLdh() + "");
                                    tv_sh2.setText(beans.getSh() + "");


                                    if (!TextUtils.isEmpty(beans.getCsqName())) {
                                        tv_sscsq.setText(beans.getCsqName());
                                    }

                                    if (!TextUtils.isEmpty(beans.getXqName())) {
                                        tv_xqmc.setText(beans.getXqName());
                                    }

                                    if (!TextUtils.isEmpty(beans.getYzxm())) {
                                        tv_yzxm.setText(beans.getYzxm());
                                    }
                                }


                                //租户信息

                                datas.clear();
                                datas.addAll(beans0.getZhxx());
                                adapter.notifyDataSetChanged();

                            }
                        });
                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            ToastUtil.shortToast(ZhListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZhListActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
        }
    }

}
