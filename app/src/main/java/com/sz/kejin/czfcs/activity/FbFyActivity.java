package com.sz.kejin.czfcs.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
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
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.FjhBeans;
import com.sz.kejin.czfcs.bean.ImageInfo;
import com.sz.kejin.czfcs.bean.ImageUploadBean;
import com.sz.kejin.czfcs.bean.LdhBeans;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.SqBeans;
import com.sz.kejin.czfcs.bean.XqBeans;
import com.sz.kejin.czfcs.bean.ZiDianBean;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.utils.CompressBtimapUtil;
import com.sz.kejin.czfcs.utils.DialogUtils;
import com.sz.kejin.czfcs.utils.GetImageUtils;
import com.sz.kejin.czfcs.utils.OkhttpUtils;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.ActionSheet;
import com.sz.kejin.czfcs.view.MyGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.cxStrs;
import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.sdxzStrs;
import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.zflxStrs;
import static com.sz.kejin.czfcs.constant.AlertViewDataConstants.zqStrs;

public class FbFyActivity extends BasicActivity {
    private static final String TAG = "FbFyActivity";
    private ImageView iv_back;
    private EditText et_qwzj,et_mj,et_rzxx,et_fyjs;
    private TextView et_szxq,et_ldh,et_fjh,et_czfs,et_zflx,et_cx,et_sdxz,et_zq;
    private LinearLayout layout_szxq,layout_ldh,layout_fjh,layout_czfs,layout_zflx,layout_cx,layout_sdxz,layout_zq;


    private MyGridView gvPcrPto1;



    private TextView btn_tj;



    //图片相关
    private ArrayList<String> listPhoto1 = new ArrayList<>();
    private ArrayList<String> listPhotoBDAdress1 = new ArrayList<>();
    private ArrayList<String> oldAddress1 = new ArrayList<>();
    private ArrayList<String> photoAddress1 = new ArrayList<>();
    private ImageGridViewAdapter gridViewAdapter1;
    private ArrayList<MediaBean> selected1 = new ArrayList<>();

    private int type = 1;// 1 房间照片  2 车库照片  3 房产证照片

    private String sqId,xqId,ldh,fjh,fjId;
    private String [] xqStrs1;
    private String [] ldhStrs1 ;
    private String [] fjhStrs1 ;
    private ArrayList<XqBeans> xqBeans = new ArrayList<>();
    private ArrayList<LdhBeans> ldhBeans = new ArrayList<>();
    private ArrayList<FjhBeans> fjhBeans = new ArrayList<>();


    private String[] czfsStrs = new String[]{};
    private String czfs ="";

    private String srcType = IntentConstants.ADD;
    private String bid = "";



    @Override
    protected int getLayoutId() {
        return R.layout.activity_fb_fy;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);

        et_szxq = findViewById(R.id.et_fbfy_szxq);
        et_ldh = findViewById(R.id.et_fbfy_ldh);
        et_fjh = findViewById(R.id.et_fbfy_fjh);
        et_qwzj = findViewById(R.id.et_fbfy_qwzj);
        et_czfs = findViewById(R.id.et_fbfy_czfs);
        et_zflx = findViewById(R.id.et_fbfy_zflx);
        et_mj = findViewById(R.id.et_fbfy_mj);
        et_cx = findViewById(R.id.et_fbfy_cx);
        et_sdxz = findViewById(R.id.et_fbfy_sdxz);
        et_zq = findViewById(R.id.et_fbfy_zq);
        et_rzxx = findViewById(R.id.et_fbfy_rzxx);
        et_fyjs = findViewById(R.id.et_fbfy_fyjs);

        layout_szxq = findViewById(R.id.layout_szxq);
        layout_ldh = findViewById(R.id.layout_ldh);
        layout_fjh = findViewById(R.id.layout_fjh);
        layout_czfs = findViewById(R.id.layout_czfs);
        layout_zflx = findViewById(R.id.layout_zflx);
        layout_cx = findViewById(R.id.layout_cx);
        layout_sdxz = findViewById(R.id.layout_sdxz);
        layout_zq = findViewById(R.id.layout_zq);


        gvPcrPto1 = findViewById(R.id.gv_pcr_pto1);
        btn_tj = findViewById(R.id.tv_tj);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        sqId = getIntent().getStringExtra(IntentConstants.SQ_DATA);
        srcType = getIntent().getStringExtra(IntentConstants.EDIT_OR_ADD_TYPE);

        if (IntentConstants.ADD.equals(srcType)) {//发布房源
            setTitle("发布房源");
        } else if (IntentConstants.EDIT.equals(srcType)) {//修改房源信息
            setTitle("修改房源信息");
            fjId = getIntent().getIntExtra(IntentConstants.FJ_DATA, 0) + "";
            getFjInfoDetail();
        }
        getXqDatas();

        getCzfsList();

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

    }
    private MainFyXxListBeans fjDetailBeans;

    private void
    getFjInfoDetail(){
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
//                            if (hud != null && hud.isShowing()) {
//                                hud.dismiss();
//                            }
                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (dataResult.getData().size() > 0) {
                                    fjDetailBeans = dataResult.getData().get(0);
                                    et_szxq.setText(fjDetailBeans.getXqName());
                                    xqId = fjDetailBeans.getXqbh();

                                    et_ldh.setText(fjDetailBeans.getLdh() + "");
                                    ldh = fjDetailBeans.getLdh() + "";

                                    et_fjh.setText(fjDetailBeans.getSh() + "");
                                    fjh = fjDetailBeans.getSh() + "";

                                    et_qwzj.setText(fjDetailBeans.getZj() );
                                    et_czfs.setText(fjDetailBeans.getCzfs());
                                    czfs = fjDetailBeans.getCzfs();
                                    et_zflx.setText(fjDetailBeans.getZflx());



                                    et_mj.setText(fjDetailBeans.getMj() + "");
                                    et_cx.setText(fjDetailBeans.getCx());

                                    et_sdxz.setText(fjDetailBeans.getSdxz());
                                    et_zq.setText(fjDetailBeans.getZq());
                                    et_rzxx.setText(fjDetailBeans.getRzxx());
                                    et_fyjs.setText(fjDetailBeans.getFjqk());

                                    bid = fjDetailBeans.getBid();

                                    getLdhDatas();
                                    getFjhDatas();



                                    if (!TextUtils.isEmpty(fjDetailBeans.getFjzp())) {

                                        String[] str1 = fjDetailBeans.getFjzp().split(",");

                                        for (int i = 0; i < str1.length; i++) {
                                            listPhotoBDAdress1.add(str1[i]);
                                        }

                                        gridViewAdapter1.notifyDataSetChanged();

                                    }
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private void getXqDatas(){


        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(sqId)) {
            return;
        }
        params.put("csqLoginid", sqId);


        OkHttpHelper.enqueue(OkHttpHelper.GET_XQ_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<XqBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<XqBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> xqDatas = new ArrayList<>();
                            if (dataResult != null && dataResult.getCode() == 1) {

                                for (int i = 0; i <dataResult.getData().size() ; i++) {
                                    xqDatas.add(dataResult.getData().get(i).getXqName());
                                }
                                xqStrs1 = new String[]{};
                                xqStrs1 = xqDatas.toArray(xqStrs1);
                                xqBeans.clear();
                                xqBeans.addAll(dataResult.getData());
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }

    private void getLdhDatas(){


        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(sqId)) {
            return;
        }
        params.put("csqLoginid", sqId);
        params.put("xqbh", xqId);


        OkHttpHelper.enqueue(OkHttpHelper.GET_LDH_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<LdhBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<LdhBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                ArrayList<String> ldhDatas = new ArrayList<>();
                                for (int i = 0; i < dataResult.getData().size(); i++) {
                                    ldhDatas.add(dataResult.getData().get(i).getLdh() + "");
                                }
                                ldhStrs1 = new String[]{};
                                ldhStrs1 = ldhDatas.toArray(ldhStrs1);
                                ldhBeans.clear();
                                ldhBeans.addAll(dataResult.getData());
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
                        }
                    }
                });
            }
        });

    }



    private void getFjhDatas(){


        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(sqId)) {
            return;
        }
        params.put("csqLoginid", sqId);
        params.put("xqbh", xqId);
        params.put("ldh", ldh);


        OkHttpHelper.enqueue(OkHttpHelper.GET_FJH_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {
                try {
                    String json = response.body().string();
                    final BaseBean<ArrayList<FjhBeans>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<FjhBeans>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    //更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {
                                ArrayList<String> fjhDatas = new ArrayList<>();
                                Log.i(TAG, "run: 11111" + dataResult.getData().size());
                                for (int i = 0; i <dataResult.getData().size() ; i++) {
                                    fjhDatas.add(dataResult.getData().get(i).getSh() + "");
                                }

                                fjhStrs1 = new String[]{};
                                fjhStrs1 = fjhDatas.toArray(fjhStrs1);
                                fjhBeans.clear();
                                fjhBeans.addAll(dataResult.getData());
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
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
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Intent intent = new Intent(FbFyActivity.this, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("list", listPhotoBDAdress1);//图片的集合
                startActivity(intent);
            }
        });


        layout_szxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择小区", null, null,null, xqStrs1,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_szxq.setText(xqStrs1[position]);
                        xqId = xqBeans.get(position).getXqbh();
                        et_ldh.setText("");
                        et_fjh.setText("");

                        getLdhDatas();

                    }
                }).setCancelable(true).show();
            }
        });

        layout_ldh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_szxq.getText().toString().trim())) {
                    ToastUtil.shortToast(FbFyActivity.this,"请先选择小区");
                    return;
                }
                new AlertView("请选择楼栋号", null, null,null, ldhStrs1,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_ldh.setText(ldhStrs1[position]);
                        ldh = ldhStrs1[position];
                        et_fjh.setText("");
                        getFjhDatas();

                    }
                }).setCancelable(true).show();
            }
        });


        layout_fjh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_szxq.getText().toString().trim())) {
                    ToastUtil.shortToast(FbFyActivity.this,"请先选择小区");
                    return;
                }

                if (TextUtils.isEmpty(et_ldh.getText().toString().trim())) {
                    ToastUtil.shortToast(FbFyActivity.this,"请先选择楼栋号");
                    return;
                }

                new AlertView("请选择房间号", null, null,null, fjhStrs1,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_fjh.setText(fjhStrs1[position]);
                        fjh = fjhStrs1[position];
                        fjId = fjhBeans.get(position).getId();

                    }
                }).setCancelable(true).show();


            }
        });



        layout_czfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择出租方式", null, null,null,czfsStrs ,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_czfs.setText(czfsStrs[position]);
                        czfs = czfsVals.get(position);

                    }
                }).setCancelable(true).show();
            }
        });

        layout_zflx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择支付类型", null, null,null, zflxStrs,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_zflx.setText(zflxStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });


        layout_cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择朝向", null, null,null, cxStrs,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_cx.setText(cxStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });

        layout_sdxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择水电性质", null, null,null, sdxzStrs,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_sdxz.setText(sdxzStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });






        layout_zq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("请选择租期", null, null,null, zqStrs,  FbFyActivity.this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Log.i(TAG, "onItemClick: ");
                        et_zq.setText(zqStrs[position]);

                    }
                }).setCancelable(true).show();
            }
        });


        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listPhotoBDAdress1.size() <= 1) {
                    ToastUtil.shortToast(FbFyActivity.this,"请至少拍2张照片");
                    return;
                }

                ImageView imageView = new ImageView(FbFyActivity.this);
                imageView.setImageResource(R.drawable.suprise);
                hud = KProgressHUD.create(FbFyActivity.this)
                        .setCustomView(imageView)
                        .setLabel("正在保存...")
                        .setDimAmount(0.5f)
                        .setCancellable(true)
                        .show();


                DialogUtils.scheduleDismiss(hud,5000);

                startThreadCompressAndUpload();
            }
        });
    }

    private void uploadFyInfo(){
        HashMap<String, String> params = new HashMap<>();
        params.put("action", srcType);

        params.put("roomid", fjId);
        params.put("bid", bid);
        params.put("csqLoginid", sqId);

        if (TextUtils.isEmpty(et_szxq.getText().toString())) {
            ToastUtil.shortToast(FbFyActivity.this,"请先选择小区");
            return;
        }
        params.put("xqbh", xqId);

        if (TextUtils.isEmpty(et_ldh.getText().toString())) {
            ToastUtil.shortToast(FbFyActivity.this,"请先选择楼栋号");
            return;
        }
        params.put("ldh", ldh);

        if (TextUtils.isEmpty(et_fjh.getText().toString())) {
            ToastUtil.shortToast(FbFyActivity.this,"请先选择房间号");
            return;
        }
        params.put("sh", fjh);


        String id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String xm = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        String lxdh = SPUtils.getInstance(this).getString(SPConstants.USER_LXDH,"");
        String sfzh = SPUtils.getInstance(this).getString(SPConstants.USER_SFZH,"");
        params.put("id", id);
        params.put("fdxm", xm);
        params.put("fdlxdh", lxdh);
        params.put("fdsfzh", sfzh);


        params.put("zj", et_qwzj.getText().toString());
        params.put("zflx",et_zflx.getText().toString());
        params.put("czfs", czfs);
        params.put("mj", et_mj.getText().toString());
        params.put("cx", et_cx.getText().toString());
        params.put("sdxz",et_sdxz.getText().toString());
        params.put("zq", et_zq.getText().toString());




        String path = "";
        for (int i = 0; i < listPhoto1.size(); i++) {
            if (i == listPhoto1.size() - 1) {
                path += listPhoto1.get(i);
            } else {
                path += listPhoto1.get(i) + "|";
            }
        }

        params.put("fjzpPath", path);
        params.put("rzxx", et_rzxx.getText().toString());
        params.put("fjxx", et_fyjs.getText().toString());

        btn_tj.setClickable(false);


        OkHttpHelper.enqueue(OkHttpHelper.FB_FY_INFO, params, new OkHttpHelper.Callback() {
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
                            btn_tj.setClickable(true);
                            if (hud != null && hud.isShowing()) {
                                hud.dismiss();
                            }

                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
                                }
                                finish();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FbFyActivity.this, dataResult.getMsg());
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
                        btn_tj.setClickable(true);
                        if (hud != null && hud.isShowing()) {
                            hud.dismiss();
                        }
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtil.shortToast(FbFyActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }



    private void startCamera(){
        ActionSheet actionSheet = new ActionSheet(FbFyActivity.this);
        actionSheet.setCancelButtonTitle("取消");
        actionSheet.addItems("拍照", "打开相册");
        actionSheet.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(ActionSheet actionSheet, int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        if (PermissionCheckUtils.checkCameraPermission(FbFyActivity.this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)) {
                            RxGalleryFinalApi.openZKCamera(FbFyActivity.this);
                        }
                        break;
                    case 1:
                        switch (type) {
                            case 1:
                                RxGalleryFinal.with(FbFyActivity.this).image().multiple().maxSize(GetImageUtils.UPLOAD_MAX_NUM - photoAddress1.size() ).imageLoader(ImageLoaderType.GLIDE).selected(selected1)
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
                        }
                        break;
                }
            }
        });
        actionSheet.setCancelableOnTouchMenuOutside(true);
        actionSheet.showMenu();
    }


    private KProgressHUD hud ;


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
                uploadFyInfo();
//                if (index2 < listPhotoBDAdress2.size()) {
//                    String fileStr = listPhotoBDAdress2.get(index2);
//                    Message message = new Message();
//                    message.what = 5;
//                    message.obj = fileStr;
//                    message.arg1 = 2;
//                    handler.sendMessage(message);
//                } else {
//                    uploadYhInfo();
//                }
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
            }
            startThreadCompressAndUpload();
        } else {
            CompressBtimapUtil.compressBitmap(bitmap, imgPath, new CompressBtimapUtil.OnImgPressListener() {
                @Override
                public void imgPressListener(File file) {
                    if (file == null) {
                        ToastUtil.shortToast(FbFyActivity.this,"图片压缩失败,请重试");
                        return;
                    }

                    HashMap<String, File> map = new HashMap<>();
                    HashMap<String, String> hashMap = new HashMap<>();
                    map.put("file", file);
                    OkhttpUtils.postFile(FbFyActivity.this, OkHttpHelper.UPLOAD_URL, ImageUploadBean.class, map,
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
