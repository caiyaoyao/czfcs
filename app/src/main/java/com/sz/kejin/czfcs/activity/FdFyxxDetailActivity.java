package com.sz.kejin.czfcs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.ImagesPagerAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.ImageInfo;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Response;

public class FdFyxxDetailActivity extends BasicActivity {
    private static final String TAG = "FdFyxxDetailActivity";
    private ImageView iv_back;


    private ViewPager mViewPager;
    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList = new ArrayList<>();
    private TextView tv_dq_img;
    private ImagesPagerAdapter adapter;

    private LinearLayout layout_xgfjxx,layout_xgzhxx;

    private int fjId;

    private TextView tv_fjxx,tv_fz,tv_fzlx,tv_mj,tv_cx,tv_fb,tv_sssq,tv_sd,tv_lc,tv_zq,tv_rzyq,tv_fyjs,tv_rs;







    @Override
    protected int getLayoutId() {
        return R.layout.activity_fd_fyxx_detail;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);


        mViewPager = findViewById(R.id.vp_img_main);
        tv_dq_img = findViewById(R.id.tv_img_wz);


        layout_xgfjxx = findViewById(R.id.layout_xgfjxx);
        layout_xgzhxx = findViewById(R.id.layout_xgzhxx);


        tv_fjxx = findViewById(R.id.tv_fjxx);
        tv_fz = findViewById(R.id.tv_fz);
        tv_fzlx = findViewById(R.id.tv_fz_lx);
        tv_mj = findViewById(R.id.tv_mj);
        tv_cx = findViewById(R.id.tv_cx);
        tv_fb = findViewById(R.id.tv_fb);
        tv_sssq = findViewById(R.id.tv_sssq);
        tv_sd = findViewById(R.id.tv_sd);
//        tv_lc = findViewById(R.id.tv_lc);
        tv_zq = findViewById(R.id.tv_zq);
        tv_rzyq = findViewById(R.id.tv_rzyq);
        tv_fyjs = findViewById(R.id.tv_fyjs);
        tv_rs = findViewById(R.id.tv_rs);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("房源详情");


    }

    @Override
    protected void onResume() {
        super.onResume();
        fjId = getIntent().getIntExtra(IntentConstants.FJ_DATA, 0);

        getFjInfoDetail();
    }

    private MainFyXxListBeans fjDetailBeans;


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
//                            if (hud != null && hud.isShowing()) {
//                                hud.dismiss();
//                            }
                            if (dataResult != null && dataResult.getCode() == 1) {
                                if (dataResult.getData().size() > 0) {
                                    fjDetailBeans = dataResult.getData().get(0);
                                    tv_fjxx.setText(fjDetailBeans.getCzfs()+  "  " + fjDetailBeans.getXqName() + "  " +  fjDetailBeans.getHx());
                                    tv_fz.setText(fjDetailBeans.getZj() + "元/月");
                                    tv_fzlx.setText(fjDetailBeans.getZflx());
                                    tv_mj.setText(fjDetailBeans.getMj() + "m²");
                                    tv_cx.setText(fjDetailBeans.getCx());
                                    tv_fb.setText(fjDetailBeans.getFbsj());
                                    tv_sssq.setText(fjDetailBeans.getCsqName());
                                    tv_sd.setText(fjDetailBeans.getSdxz());
                                    tv_zq.setText(fjDetailBeans.getZq());
                                    tv_rzyq.setText(fjDetailBeans.getRzxx());
                                    tv_fyjs.setText(fjDetailBeans.getFjqk());

                                    tv_rs.setText(fjDetailBeans.getZksl() + "");





                                    if (!TextUtils.isEmpty(fjDetailBeans.getFjzp())) {

                                        String[] str1 = fjDetailBeans.getFjzp().split(",");
                                        imageInfoList.clear();

                                        for (int i = 0; i < str1.length; i++) {
                                            imageInfoList.add(new ImageInfo(OkHttpHelper.ROOT + str1[i]));
                                        }


                                        //设置图片轮播
//                                        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,
//                                                R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
                                        String[] titles = new String[imageInfoList.size()];
                                        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

                                        for (int i = 0; i < imageInfoList.size(); i++) {
                                            titles[i] = imageInfoList.get(i).getTitle();
                                            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(FdFyxxDetailActivity.this);
                                            simpleDraweeView.setAspectRatio(1.78f);
                                            // 设置一张默认的图片
                                            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(FdFyxxDetailActivity.this.getResources())
                                                    .setPlaceholderImage(ContextCompat.getDrawable(FdFyxxDetailActivity.this, R.mipmap.fjzp_mr), ScalingUtils.ScaleType.CENTER_CROP).build();
                                            simpleDraweeView.setHierarchy(hierarchy);
                                            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

                                            //加载高分辨率图片;
                                            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                                                    .setResizeOptions(new ResizeOptions(1280, 720))
                                                    .build();
                                            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                                                    //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                                                    .setImageRequest(imageRequest)
                                                    .setOldController(simpleDraweeView.getController())
                                                    .build();
                                            simpleDraweeView.setController(controller);

                                            simpleDraweeView.setId(imageInfoList.get(i).getId());//给view设置id
                                            simpleDraweeView.setTag(imageInfoList.get(i));
                                            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(FdFyxxDetailActivity.this, ImagePreviewActivity.class);
                                                    ArrayList<String> listPhotoBDAdress3 = new ArrayList<>();
                                                    for (int j = 0; j <imageInfoList.size() ; j++) {
                                                        listPhotoBDAdress3.add(imageInfoList.get(j).getImage());
                                                    }
                                                    intent.putStringArrayListExtra("list", listPhotoBDAdress3);//图片的集合
                                                    startActivity(intent);
                                                }
                                            });
                                            titles[i] = imageInfoList.get(i).getTitle();
                                            simpleDraweeViewList.add(simpleDraweeView);

                                        }


                                        adapter = new ImagesPagerAdapter(simpleDraweeViewList, mViewPager, FdFyxxDetailActivity.this);
                                        mViewPager.setAdapter(adapter);
                                        tv_dq_img.setText("1/" + imageInfoList.size() );
                                    }
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FdFyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FdFyxxDetailActivity.this, errorMsg);
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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: " + position);
                tv_dq_img.setText(((position) % (imageInfoList.size()) + 1)   + "/" + imageInfoList.size() );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        layout_xgfjxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FdFyxxDetailActivity.this, FbFyActivity.class);
                intent.putExtra(IntentConstants.EDIT_OR_ADD_TYPE, IntentConstants.EDIT);
                intent.putExtra(IntentConstants.SQ_DATA, fjDetailBeans.getCsqLoginid());
                intent.putExtra(IntentConstants.FJ_DATA, fjId);
                startActivity(intent);
            }
        });


        layout_xgzhxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FdFyxxDetailActivity.this, ZhListActivity.class);
                intent.putExtra(IntentConstants.FJ_DATA, fjId);
                startActivity(intent);
            }
        });

    }

}
