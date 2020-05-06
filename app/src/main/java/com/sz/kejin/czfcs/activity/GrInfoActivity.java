package com.sz.kejin.czfcs.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.GrxxListAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.GrxxListItemBeans;
import com.sz.kejin.czfcs.bean.ImageUploadBean;
import com.sz.kejin.czfcs.bean.SqBeans;
import com.sz.kejin.czfcs.bean.UserInfoBean;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.manager.SyLinearLayoutManager;
import com.sz.kejin.czfcs.utils.CompressBtimapUtil;
import com.sz.kejin.czfcs.utils.OkhttpUtils;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.ActionSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrInfoActivity extends BasicActivity {
    private static final String TAG = "GrInfoActivity";
    private ImageView iv_back;
    private CircleImageView circleImageView;
    private int type = 1;// 1 更换头像
    private TextView tv_tc,tv_yhm,tv_sjh,tv_num,tv_fbfy;


    private RecyclerView rv_grxx;
    private GrxxListAdapter adapter;
    private ArrayList<GrxxListItemBeans> datas = new ArrayList<>();
    private String[] strs = new String[]{"修改信息","我的房源","我的收藏","我的评论","关于我们"};
    private int[] imgIds = new int[]{R.mipmap.grxx_xgxx,R.mipmap.grxx_wdfy,R.mipmap.grxx_wdsc,R.mipmap.grxx_hf,R.mipmap.grxx_gywm};

    private LinearLayout layout_yc;
    private String user_type = Constants.USER_TYPE_YK;

    //图片相关
    private ArrayList<String> listPhoto1 = new ArrayList<>();


    private ArrayList<String> listPhotoBDAdress1 = new ArrayList<>();

    private String id = "";




    @Override
    protected int getLayoutId() {
        return R.layout.activity_gr_info;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);
        circleImageView = findViewById(R.id.civ_grtx);
        tv_tc = findViewById(R.id.tv_tc);
        tv_yhm = findViewById(R.id.tv_yhm);
        tv_sjh = findViewById(R.id.tv_sjh);
        tv_num = findViewById(R.id.tv_gr_num);
        tv_fbfy = findViewById(R.id.tv_fbfy);

        layout_yc = findViewById(R.id.layout_yc);


        rv_grxx = findViewById(R.id.rv_grxx);
        rv_grxx.setLayoutManager(new SyLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

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
                                if (dataResult.getData() != null && dataResult.getData().size() > 0) {
                                    UserInfoBean userInfoBean = dataResult.getData().get(0);
                                    tv_yhm.setText(userInfoBean.getXm());
                                    tv_sjh.setText(userInfoBean.getLxdh());

                                    if (!TextUtils.isEmpty(userInfoBean.getFwsl())) {
                                        tv_num.setText("您已发布房源" + userInfoBean.getFwsl() + "套");
                                    }


                                    Glide.with(GrInfoActivity.this).load(OkHttpHelper.ROOT + userInfoBean.getZpPath())
                                            .override(60,60)
                                            .error(R.mipmap.hl_new_card_no_no)
                                            .into(circleImageView);
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(GrInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(GrInfoActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("个人信息");

        for (int i = 0; i <imgIds.length ; i++) {
            GrxxListItemBeans bean = new GrxxListItemBeans(strs[i],imgIds[i]);
            datas.add(bean);
        }
        adapter = new GrxxListAdapter(datas);
        rv_grxx.setAdapter(adapter);


        id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        user_type = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,Constants.USER_TYPE_YK);
        if (Constants.USER_TYPE_YK.equals(user_type)) {
            layout_yc.setVisibility(View.INVISIBLE);
        } else if (Constants.USER_TYPE_FD.equals(user_type)) {
            layout_yc.setVisibility(View.VISIBLE);
        }
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

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//更换头像
                type =1;
                index = 0;
                listPhotoBDAdress1.clear();
                listPhoto1.clear();
                startCamera();
            }
        });

        tv_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//退出登录，清除保存的数据
                SPUtils.getInstance(GrInfoActivity.this).clear();
                finish();
            }
        });

        tv_fbfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrInfoActivity.this,SqListActivity.class));
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                switch (position) {
                    case 0://修改信息
                        startActivity(new Intent(GrInfoActivity.this,XiugaiXxActivity.class));
                        finish();
                        break;
                    case 1://我的房源
                        if (Constants.USER_TYPE_YK.equals(user_type)) {
                            ToastUtil.shortToast(GrInfoActivity.this, getString(R.string.fd_type_ts));
                        } else {
                            startActivity(new Intent(GrInfoActivity.this,WdFyListActivity.class));
                        }
                        break;
                    case 2://我的收藏
                        startActivity(new Intent(GrInfoActivity.this,WdScListActivity.class));
                        break;
                    case 3://我的评论
                        startActivity(new Intent(GrInfoActivity.this,WdPlActivity.class));
                        break;
                    case 4://关于我们
                        startActivity(new Intent(GrInfoActivity.this,AboutUsActivity.class));
                        break;
               }
            }
        });
    }

    private void startCamera(){
        ActionSheet actionSheet = new ActionSheet(GrInfoActivity.this);
        actionSheet.setCancelButtonTitle("取消");
        actionSheet.addItems("拍照", "打开相册");
        actionSheet.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(ActionSheet actionSheet, int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        if (PermissionCheckUtils.checkCameraPermission(GrInfoActivity.this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)) {
                            RxGalleryFinalApi.openZKCamera(GrInfoActivity.this);
                        }
                        break;
                    case 1:
                        switch (type) {
                            case 1:
                                RxGalleryFinal.with(GrInfoActivity.this).image().multiple().maxSize(1).imageLoader(ImageLoaderType.GLIDE)
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
//                    Glide.with(this).load(img)
//                            .override(60,60)
//                            .into(circleImageView);
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
                                    ToastUtil.shortToast(GrInfoActivity.this, dataResult.getMsg());
                                }
                                String path = dataResult.getData();
                                Log.i(TAG, "run: 1111111"  + path);

                                Glide.with(GrInfoActivity.this).load(OkHttpHelper.ROOT + path)
                                        .override(60,60)
                                        .error(R.mipmap.hl_new_card_no_no)
                                        .into(circleImageView);
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(GrInfoActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(GrInfoActivity.this, errorMsg);
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
                        ToastUtil.shortToast(GrInfoActivity.this,"图片压缩失败,请重试");
                        return;
                    }

                    HashMap<String, File> map = new HashMap<>();
                    HashMap<String, String> hashMap = new HashMap<>();
                    map.put("file", file);
                    OkhttpUtils.postFile(GrInfoActivity.this, OkHttpHelper.UPLOAD_URL, ImageUploadBean.class, map,
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
