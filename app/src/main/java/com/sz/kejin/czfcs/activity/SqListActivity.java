package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.SqRecyclerTabAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.SqBeans;
import com.sz.kejin.czfcs.bean.XqBeans;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.MainViewPager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import okhttp3.Call;
import okhttp3.Response;

public class SqListActivity extends BasicActivity {
    private static final String TAG = "SqListActivity";
    private TextView tv_back;
    private ImageView iv_back;
    private RecyclerView rv_xqlb;
    private SqRecyclerTabAdapter adapter;
    private ArrayList<SqBeans> datas = new ArrayList<>();



    private  int spacing = 20;
    private  int spanCount = 2;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sq_list;
    }

    @Override
    protected void initView() {
        tv_back = findViewById(R.id.tv_back);
        iv_back = findViewById(R.id.iv_title_back);


        rv_xqlb = findViewById(R.id.rv_sq_list);
        rv_xqlb.setLayoutManager(new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL));
        rv_xqlb.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,false));


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("请选择社区");

        datas = new ArrayList<>();

        adapter = new SqRecyclerTabAdapter(datas);
        rv_xqlb.setAdapter(adapter);
        getSqDatas();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(SqListActivity.this, FbFyActivity.class);
                intent.putExtra(IntentConstants.SQ_DATA, datas.get(position).getCsqid());
                intent.putExtra(IntentConstants.EDIT_OR_ADD_TYPE, IntentConstants.ADD);
                startActivity(intent);
            }
        });

    }


    private void getSqDatas(){


        HashMap<String, String> params = new HashMap<>();


        OkHttpHelper.enqueue(OkHttpHelper.GET_SQ, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<SqBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<SqBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                        //更新UI
                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (dataResult != null && dataResult.getCode() == 1) {
                                    datas.clear();
                                    datas.addAll(dataResult.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                        ToastUtil.shortToast(SqListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(SqListActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

}
