package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.FjlbLdhRecyclerTabAdapter;
import com.sz.kejin.czfcs.adapter.FyListLoadMoreAdapter;
import com.sz.kejin.czfcs.adapter.GuideAdapter;
import com.sz.kejin.czfcs.adapter.ListGridAdapter;
import com.sz.kejin.czfcs.adapter.MainRecyclerTabAdapter;
import com.sz.kejin.czfcs.adapter.MainRvFyxxListAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.SqBeans;
import com.sz.kejin.czfcs.bean.ZiDianBean;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.CustomLoadMoreView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;
import okhttp3.Call;
import okhttp3.Response;


public class ZzFyListActivity extends BasicActivity {
    private static final String TAG = "ZzFyListActivity";

    class PageInfo {
        int page = 1;

        void nextPage() {
            page++;
        }

        void reset() {
            page = 1;
        }

        boolean isFirstPage() {
            return page == 1;
        }
    }

    private static final int PAGE_SIZE = 10;

    private ImageView iv_title_left;





    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv_main_fyxx;
    private FyListLoadMoreAdapter adapter;
    private ArrayList<MainFyXxListBeans> datas = new ArrayList<>();
    private PageInfo pageInfo = new PageInfo();

    private LinearLayout layout1,layout2,layout3,layout4;
    private TextView tv1,tv2,tv3,tv4;

    private String [] sqStrs1;
    private String[] zjStrs = new String[]{};
    private String[] hxStrs = new String[]{};
    private String[] czfsStrs = new String[]{};

    private ArrayList<SqBeans> sqBeans = new ArrayList<>();
    private String sqId = "",lxStr = "",hxStr = "", yzjStr = "";

    private EditText et_search;

    private Button btnSearch;


    private RecyclerView rv1;
    private FjlbLdhRecyclerTabAdapter adapter1;
    private ArrayList<BasicTableItemBean> tableItemData1 = new ArrayList<>();
    private String[] qyStrs = new String[]{"不限","蓬朗","兵希","孔巷"};

    private String keyword = "";





    @Override
    protected int getLayoutId() {
        return R.layout.activity_zz_fy_list;
    }

    @Override
    protected void initView() {
        iv_title_left = findViewById(R.id.iv_title_back);

        et_search = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);


        rv1 = findViewById(R.id.rv_qy);
        rv1.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));

        layout1 = findViewById(R.id.fy_layout1);
        layout2 = findViewById(R.id.fy_layout2);
        layout3 = findViewById(R.id.fy_layout3);
        layout4 = findViewById(R.id.fy_layout4);

        tv1 = findViewById(R.id.fy_tv1);
        tv2 = findViewById(R.id.fy_tv2);
        tv3 = findViewById(R.id.fy_tv3);
        tv4 = findViewById(R.id.fy_tv4);

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        rv_main_fyxx = findViewById(R.id.rv_main_fyxx);
        rv_main_fyxx.setLayoutManager(new SyLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_main_fyxx.setHasFixedSize(true);
        rv_main_fyxx.setNestedScrollingEnabled(false);
        rv_main_fyxx.addItemDecoration(new GridSpacingItemDecoration(1,40,false));


    }




    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("房源列表");

        getSqDatas();
        getYzjList();
        getHxList();
        getCzfsList();


        for (int i = 0; i < qyStrs.length; i++) {
            BasicTableItemBean item = new BasicTableItemBean(i + 1,qyStrs[i]);
            tableItemData1.add(i, item);
        }


        adapter1 = new FjlbLdhRecyclerTabAdapter(tableItemData1,this);
        rv1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        adapter = new FyListLoadMoreAdapter(this);
        adapter.setAnimationEnable(true);
        rv_main_fyxx.setAdapter(adapter);

        initRefreshLayout();
        initLoadMore();

    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 进入页面，刷新数据
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        adapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    /**
     * 刷新
     */
    private void refresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        adapter.getLoadMoreModule().setEnableLoadMore(false);
        // 下拉刷新，需要重置页数
        pageInfo.reset();
        request();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        request();
    }



    private void request(){
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", pageInfo.page + "");
        params.put("pageSize", PAGE_SIZE + "");
        params.put("csqid", sqId);
        params.put("lx",lxStr);
        params.put("hx",hxStr);
        params.put("yzj", yzjStr);

        if (!TextUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        } else {
            params.put("keyword", et_search.getText().toString());
        }


        OkHttpHelper.enqueue(OkHttpHelper.GET_FY_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<MainFyXxListBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<MainFyXxListBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);

                                if (pageInfo.isFirstPage()) {
                                    //如果是加载的第一页数据，用 setData()
                                    datas.clear();
                                    datas.addAll(dataResult.getData());
                                    adapter.setList(dataResult.getData());
                                } else {
                                    //不是第一页，则用add
                                    datas.addAll(dataResult.getData());
                                    adapter.addData(dataResult.getData());
                                }

                                if (dataResult.getData().size() < PAGE_SIZE) {
                                    //如果不够一页,显示没有更多数据布局
                                    adapter.getLoadMoreModule().loadMoreEnd();
                                } else {
                                    adapter.getLoadMoreModule().loadMoreComplete();
                                }

                                // page加一
                                pageInfo.nextPage();
                            }
                        });

                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(ZzFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZzFyListActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }


    private ArrayList<String> hxTexts = new ArrayList<>();
    private ArrayList<String> hxVals = new ArrayList<>();

    private void getHxList(){
        HashMap<String, String> params = new HashMap<>();


        OkHttpHelper.enqueue(OkHttpHelper.GET_HX_LIST, params, new OkHttpHelper.Callback() {
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
                                        hxTexts.add(dataResult.getData().get(i).getText());
                                        hxVals.add(dataResult.getData().get(i).getVal());
                                    }

                                    hxStrs = hxTexts.toArray(hxStrs);

                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(ZzFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZzFyListActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private ArrayList<String> yzjTexts = new ArrayList<>();
    private ArrayList<String> yzjVals = new ArrayList<>();

    private void getYzjList(){
        HashMap<String, String> params = new HashMap<>();


        OkHttpHelper.enqueue(OkHttpHelper.GET_YZJ_LIST, params, new OkHttpHelper.Callback() {
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
                                        yzjTexts.add(dataResult.getData().get(i).getText());
                                        yzjVals.add(dataResult.getData().get(i).getVal());
                                    }

                                    zjStrs = yzjTexts.toArray(zjStrs);

                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(ZzFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZzFyListActivity.this, errorMsg);
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
                                    ToastUtil.shortToast(ZzFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZzFyListActivity.this, errorMsg);
                        }
                    }
                });
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
                                ArrayList<String> sqDatas = new ArrayList<>();
                                SqBeans sqBean = new SqBeans("不限","","");
                                sqBeans.clear();
                                sqBeans.add(0, sqBean);
                                sqBeans.addAll(dataResult.getData());
                                for (int i = 0; i < sqBeans.size(); i++) {
                                    sqDatas.add(sqBeans.get(i).getName());
                                }
                                sqStrs1 = new String[]{};
                                sqStrs1 = sqDatas.toArray(sqStrs1);

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(ZzFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(ZzFyListActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }


    @Override
    protected void initListener() {
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Log.i(TAG, "onItemClick: rv1:" + position);

                if (position == 0) {
                    keyword = "";
                } else {
                    keyword = tableItemData1.get(position).getTitle();
                }

                TextView textView  = (TextView) adapter.getViewByPosition(position, R.id.mTv_tab_title);
                textView.setSelected(true);
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (adapter.getItemId(i) != adapter.getItemId(position)) {
                        TextView textView1  = (TextView) adapter.getViewByPosition(i, R.id.mTv_tab_title);
                        textView1.setSelected(false);
                    }
                }

                refresh();

            }
        });


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(ZzFyListActivity.this, FyxxDetailActivity.class);
                intent.putExtra(IntentConstants.FJ_DATA, datas.get(position).getId());
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });



        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择社区", null, null,null, sqStrs1,  ZzFyListActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        tv1.setText(sqStrs1[position]);
                        sqId = sqBeans.get(position).getCsqid();
                        refresh();

                    }
                }).setCancelable(true).show();
            }
        });




        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择类型", null, null,null,czfsStrs ,  ZzFyListActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        tv2.setText(czfsStrs[position]);
                        lxStr = czfsVals.get(position);
                        refresh();
                    }
                }).setCancelable(true).show();
            }
        });




        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择类型", null, null,null, hxStrs,  ZzFyListActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        tv3.setText(hxStrs[position]);
                        hxStr = hxVals.get(position);
                        refresh();
                    }
                }).setCancelable(true).show();
            }
        });




        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择类型", null, null,null, zjStrs,  ZzFyListActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        tv4.setText(zjStrs[position]);
                        yzjStr = yzjVals.get(position);
                        refresh();
                    }
                }).setCancelable(true).show();
            }
        });


    }

}
