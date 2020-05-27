package com.sz.kejin.czfcs.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.FyListLoadMoreAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.HashMap;

public class WdFyListActivity extends BasicActivity {
    private static final String TAG = "WdFyListActivity";


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

    private EditText et_search;

    private Button btnSearch;

    private int srcType = IntentConstants.WDFY;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wd_fy_list;
    }

    @Override
    protected void initView() {
        iv_title_left = findViewById(R.id.iv_title_back);

        et_search = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        rv_main_fyxx = findViewById(R.id.rv_main_fyxx);
        rv_main_fyxx.setLayoutManager(new SyLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv_main_fyxx.setHasFixedSize(true);
        rv_main_fyxx.setNestedScrollingEnabled(false);
        rv_main_fyxx.addItemDecoration(new GridSpacingItemDecoration(1,40,false));

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        srcType = getIntent().getIntExtra(IntentConstants.WDFY_LIST_SRC, IntentConstants.WDFY);

        switch (srcType) {
            case IntentConstants.DJZK:
                setTitle("登记租客");
                break;
            case IntentConstants.WDFY:
                setTitle("我的房源");
                break;
        }


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
        String sfzh = SPUtils.getInstance(this).getString(SPConstants.USER_SFZH,"");
        params.put("sfzh", sfzh);
        params.put("keyword", et_search.getText().toString());

        OkHttpHelper.enqueue(OkHttpHelper.GET_WDFY_LIST, params, new OkHttpHelper.Callback() {
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
                                    ToastUtil.shortToast(WdFyListActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(WdFyListActivity.this, errorMsg);
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

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                Intent intent = null;

                switch (srcType) {
                    case IntentConstants.DJZK:
                        intent = new Intent(WdFyListActivity.this, ZhListActivity.class);
                        break;
                    case IntentConstants.WDFY:
                        intent = new Intent(WdFyListActivity.this, FdFyxxDetailActivity.class);
                        break;
                }

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
    }
}
