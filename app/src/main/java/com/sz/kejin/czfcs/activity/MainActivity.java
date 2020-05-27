package com.sz.kejin.czfcs.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.activity.BasicActivity;
import com.sz.kejin.czfcs.adapter.GuideAdapter;
import com.sz.kejin.czfcs.adapter.ListGridAdapter;
import com.sz.kejin.czfcs.adapter.MainRecyclerTabAdapter;
import com.sz.kejin.czfcs.adapter.MainRvFyxxListAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.UpdateAppBean;
import com.sz.kejin.czfcs.bean.VersionBean;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.manager.UpdateManager;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BasicActivity {
    private static final String TAG = "MainActivity";

    private ImageView iv_title_right,iv_title_left;
    private TextView tv_back,tv_xm;
    private ImageView iv_gr;

    private int[] mImageIds = new int[]{R.mipmap.main_banner,R.mipmap.main_banner,R.mipmap.main_banner,R.mipmap.main_banner};
    private ViewPager mViewPager;
    private CirclePageIndicator cpi_main;
    private ArrayList<ImageView> mImageViewList;
    private GuideAdapter adapter;

    private boolean isStop = false;
    private static int PAGER_TIOME = 3000;



    private RecyclerView rv_mian;
    private ArrayList<BasicTableItemBean> tableItemData1 = new ArrayList<>();
//    private String[] rvDatas1 = new String[]{"整租","合租","登记租客","发布房源"};
    private String[] rvDatas1 = new String[]{"整租","合租","登记租客","我的房源"};
    private int[] rvImgIds1 = new int[]{R.mipmap.main_zz,R.mipmap.main_hz,R.mipmap.main_djzk,R.mipmap.main_fbfy};
    private MainRecyclerTabAdapter adapter1;



    private RecyclerView rv_main_fyxx;
    private ArrayList<MainFyXxListBeans> beans = new ArrayList<>();
    private MainRvFyxxListAdapter adapter2;

    private Boolean isLogined = false;
    private String user_type = Constants.USER_TYPE_YK;
    private LinearLayout layout_user;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        iv_title_right = findViewById(R.id.iv_search);
        iv_title_right.setVisibility(View.VISIBLE);
        iv_title_right.setImageResource(R.mipmap.more);
        iv_title_left = findViewById(R.id.iv_title_back);
        iv_title_left.setVisibility(View.INVISIBLE);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setVisibility(View.INVISIBLE);

        layout_user = findViewById(R.id.layout_user);

        tv_xm = findViewById(R.id.tv_main_xm);
        iv_gr = findViewById(R.id.iv_gr);

        mViewPager = findViewById(R.id.vp_img_main);
        cpi_main = findViewById(R.id.cpi_main);


        rv_mian = findViewById(R.id.rv_sy_main);
        rv_mian.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));


        rv_main_fyxx = findViewById(R.id.rv_main_fyxx);
        rv_main_fyxx.setLayoutManager(new SyLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_main_fyxx.setHasFixedSize(true);
        rv_main_fyxx.setNestedScrollingEnabled(false);
        rv_main_fyxx.addItemDecoration(new GridSpacingItemDecoration(1,40,false));
    }


    public  String getLocalVersion(Context ctx) {
        String localVersion = "0";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
//            localVersion = packageInfo.versionCode;
            localVersion = packageInfo.versionName;
            Log.i(TAG, "getLocalVersion: " + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
    private void checkUpgrade(){
        String currentVer = "" + getLocalVersion(this);
        HashMap<String, String> params = new HashMap<>();
//        params.put("version", currentVer);

        OkHttpHelper.enqueue(OkHttpHelper.CHECK_VERSION, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<VersionBean> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<VersionBean>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    if (dataResult.getCode() == 1) {
                        if (dataResult.getData() != null && !TextUtils.isEmpty(dataResult.getData().getBbh())) {
                            if (!dataResult.getData().getBbh().equals(currentVer)) {//升级
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UpdateAppBean updateAppBean = new UpdateAppBean(dataResult.getData().getUrl(), "", dataResult.getData().getGxxx(), "1");
                                        UpdateManager.getInstance().sendRequest(MainActivity.this,updateAppBean);
                                    }
                                });
                            }
                        }

                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.shortToast(SplashActivity.this, dataResult.getMsg());
//                                }
//                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRequestFail(Call call, final String errorMsg) {
                Log.i(TAG, "onRequestFail: ");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!TextUtils.isEmpty(errorMsg)) {
//                            ToastUtil.shortToast(SplashActivity.this, errorMsg);
//                        }
//                    }
//                });
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.app_name));

        getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
            @Override
            public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                super.onPermissionGranted(grantedPermissions);
            }
        },PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE, PermissionHelper.Permission.CAMERA, PermissionHelper.Permission.ACCESS_COARSE_LOCATION);


        checkUpgrade();


        mImageViewList = new ArrayList<>();
        ImageView view;
        for (int i = 0; i < mImageIds.length; i++) {
            //创建ImageView把mImgaeViewIds放进去
            view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            //添加到ImageView的集合中
            mImageViewList.add(view);
        }
        adapter = new GuideAdapter(mImageViewList,mViewPager);
        mViewPager.setAdapter(adapter);
        cpi_main.setViewPager(mViewPager);
        autoPlayView();

        for (int i = 0; i < rvImgIds1.length; i++) {
            BasicTableItemBean item = new BasicTableItemBean(rvImgIds1[i], rvDatas1[i]);
            tableItemData1.add(i,item);
        }
        adapter1 = new MainRecyclerTabAdapter(tableItemData1);
        rv_mian.setAdapter(adapter1);

        adapter2 = new MainRvFyxxListAdapter(beans, this);
        rv_main_fyxx.setAdapter(adapter2);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        isLogined = SPUtils.getInstance(this).getBoolean(SPConstants.IS_LOGIN,false);
        user_type = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,Constants.USER_TYPE_YK);
        name = TextUtils.isEmpty(name) ? "" : name + ",";
        tv_xm.setText(name  + "您好!");
        getMainFyList();
    }

    private void getMainFyList(){
        HashMap<String, String> params = new HashMap<>();
        params.put("number", "8");

//        params.put("pageIndex",  "1");
//        params.put("pageSize", 10 + "");
//        String sfzh = SPUtils.getInstance(this).getString(SPConstants.USER_SFZH,"");
//        params.put("sfzh", sfzh);

        OkHttpHelper.enqueue(OkHttpHelper.GET_MAIN_FY_LIST, params, new OkHttpHelper.Callback() {
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
                                beans.clear();
                                beans.addAll(dataResult.getData());
                                adapter2.notifyDataSetChanged();
                            }
                        });

                    } else {
                        if (!TextUtils.isEmpty(dataResult.getMsg())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(MainActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(MainActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    SystemClock.sleep(PAGER_TIOME);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: " + mViewPager.getCurrentItem());
                            mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % mImageViewList.size());
                        }
                    });

                }
            }
        }).start();
    }

    @Override
    protected void initListener() {
        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (isLogined) {
                    List<String> listItems = new ArrayList<>();
                    listItems.add("退出登录");
//                listItems.add("修改密码");
//                listItems.add("关于我们");
                    listItems.add("个人信息");
                    View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_home_show_exit, null);
                    final PopupWindow popWnd = new PopupWindow(MainActivity.this);
                    popWnd.setContentView(contentView);
                    popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    //PopupWindow对象设置高度
                    popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    //PopupWindow对象设置获取焦点
                    popWnd.setFocusable(true);
                    //PopupWindow对象设置可以触发点击事件
                    popWnd.setTouchable(true);
                    popWnd.setOutsideTouchable(true);
                    popWnd.setBackgroundDrawable(new ColorDrawable(0));
                    popWnd.showAsDropDown(iv_title_right,0,0, Gravity.RIGHT);
                    ListView listView = (ListView)contentView.findViewById(R.id.list_home_exit_grid);
                    ListGridAdapter adapter = new ListGridAdapter(MainActivity.this,listItems);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView tex = (TextView)view.findViewById(R.id.text_list_grid_adapter);
                            if(tex.getText().toString().equals("退出登录")){
                                new AlertView("提示","确定要退出当前账号?","取消", new String[]{"确定"},null,MainActivity.this, AlertView.Style.Alert, alertViewClickListener).show();
                            }
                            if(tex.getText().toString().equals("关于我们")){
                                startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
                            }
                            if (tex.getText().toString().equals("修改密码")) {
                                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                            }

                            if (tex.getText().toString().equals("个人信息")) {
                                startActivity(new Intent(MainActivity.this, GrInfoActivity.class));
                            }
                            popWnd.dismiss();
                        }
                    });
                } else {
                    ToastUtil.shortToast(MainActivity.this,"请先登录");
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }


            }
        });



        iv_gr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined) {
                    startActivity(new Intent(MainActivity.this, GrInfoActivity.class));
                } else {
                    ToastUtil.shortToast(MainActivity.this,"请先登录");
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        });

        layout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined) {
                    startActivity(new Intent(MainActivity.this, GrInfoActivity.class));
                } else {
                    ToastUtil.shortToast(MainActivity.this,"请先登录");
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        });


        adapter1.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                switch (position) {
                    case 0://整租
                        startActivity(new Intent(MainActivity.this,ZzFyListActivity.class));
                        break;
                    case 1://合租
                        startActivity(new Intent(MainActivity.this,ZzFyListActivity.class));
                        break;
                    case 2://登记租客
                        if (isLogined) {
                            if (Constants.USER_TYPE_YK.equals(user_type)) {
                                ToastUtil.shortToast(MainActivity.this, getString(R.string.fd_type_ts));
                            } else {
                                Intent intent = new Intent(MainActivity.this, WdFyListActivity.class);
                                intent.putExtra(IntentConstants.WDFY_LIST_SRC, IntentConstants.DJZK);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtil.shortToast(MainActivity.this,"请先登录");
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }
                        break;
//                    case 3://发布房源
//                        //判断是否登录
//                        if (isLogined) {
//                            if (Constants.USER_TYPE_YK.equals(user_type)) {
//                                ToastUtil.shortToast(MainActivity.this, getString(R.string.fd_type_ts));
//                            } else {
//                                startActivity(new Intent(MainActivity.this,SqListActivity.class));
//                            }
//                        } else {
//                            ToastUtil.shortToast(MainActivity.this,"请先登录");
//                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//                        }
//                        break;

                    case 3://我的房源
                        //判断是否登录
                        if (isLogined) {
                            if (Constants.USER_TYPE_YK.equals(user_type)) {
                                ToastUtil.shortToast(MainActivity.this, getString(R.string.fd_type_ts));
                            } else {
                                Intent intent = new Intent(MainActivity.this, WdFyListActivity.class);
                                intent.putExtra(IntentConstants.WDFY_LIST_SRC, IntentConstants.WDFY);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtil.shortToast(MainActivity.this,"请先登录");
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }
                        break;
                }
            }
        });


        adapter2.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(MainActivity.this, FyxxDetailActivity.class);
                intent.putExtra(IntentConstants.FJ_DATA, beans.get(position).getId());
                startActivity(intent);
            }
        });

    }




    OnItemClickListener alertViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Object o, int position) {
            if (position == 0) {
                SPUtils.getInstance(MainActivity.this).clear();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
            }
        }
    };


}
