package com.sz.kejin.czfcs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.adapter.CommentExpandAdapter;
import com.sz.kejin.czfcs.adapter.ImagesPagerAdapter;
import com.sz.kejin.czfcs.adapter.MainRecyclerTabAdapter;
import com.sz.kejin.czfcs.adapter.ZbtsRecyclerTabAdapter;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;
import com.sz.kejin.czfcs.bean.CommentBean;
import com.sz.kejin.czfcs.bean.CommentDetailBean;
import com.sz.kejin.czfcs.bean.ImageInfo;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.ReplyDetailBean;
import com.sz.kejin.czfcs.bean.ScStatusBean;
import com.sz.kejin.czfcs.bean.UserInfoBySfzBean;
import com.sz.kejin.czfcs.constant.Constants;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.constant.SPConstants;
import com.sz.kejin.czfcs.helper.GridSpacingItemDecoration;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.helper.PermissionHelper;
import com.sz.kejin.czfcs.interfaces.OnPermissionGrantListener;
import com.sz.kejin.czfcs.utils.SPUtils;
import com.sz.kejin.czfcs.utils.ToastUtil;
import com.sz.kejin.czfcs.view.CommentExpandableListView;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Response;

public class FyxxDetailActivity extends BasicActivity {
    private static final String TAG = "FyxxDetailActivity";
    private ImageView iv_back;


    private ViewPager mViewPager;
    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList = new ArrayList<>();
    private TextView tv_dq_img;
    private ImagesPagerAdapter adapter;


    private TextView tv_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter1;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList = new ArrayList<>();
    private BottomSheetDialog dialog;

    private LinearLayout layout_sc;
    private TextView tv_sc;
    private ImageView iv_sc;

    private LinearLayout layout_lxfd;

    private int fjId;

    private TextView tv_fjxx,tv_fz,tv_fzlx,tv_mj,tv_cx,tv_fb,tv_sssq,tv_sd,tv_lc,tv_zq,tv_rzyq,tv_fyjs,tv_ld,tv_fjh;

    private Boolean isLogined = false;

    private ScaleRatingBar ratingBar;



    private int type = 0;//0 收藏  1 取消收藏/已收藏


    private TextView tv_ckwz,tv_wz;
    private RecyclerView rv_zbts;

    private ArrayList<BasicTableItemBean> tableItemData1 = new ArrayList<>();
    private String[] rvDatas1 = new String[]{"公交","地铁","学校","医院","银行","超市","商场","餐馆","酒店","景点"};
    private int[] rvImgIds1 = new int[]{R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs,R.mipmap.img_cs};
    private ZbtsRecyclerTabAdapter zbtsAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fyxx_detail;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_title_back);


        mViewPager = findViewById(R.id.vp_img_main);
        tv_dq_img = findViewById(R.id.tv_img_wz);


        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        tv_comment = findViewById(R.id.detail_page_do_comment);


        layout_sc = findViewById(R.id.layout_sc);
        tv_sc = findViewById(R.id.tv_sc);
        iv_sc = findViewById(R.id.img_sc);

        layout_lxfd = findViewById(R.id.layout_lxfd);

        tv_fjxx = findViewById(R.id.tv_fjxx);
        tv_fz = findViewById(R.id.tv_fz);
        tv_fzlx = findViewById(R.id.tv_fz_lx);
        tv_mj = findViewById(R.id.tv_mj);
        tv_cx = findViewById(R.id.tv_cx);
        tv_fb = findViewById(R.id.tv_fb);
        tv_sssq = findViewById(R.id.tv_sssq);
        tv_sd = findViewById(R.id.tv_sd);
        tv_ld = findViewById(R.id.tv_ld);
        tv_fjh = findViewById(R.id.tv_fjh);
//        tv_lc = findViewById(R.id.tv_lc);
        tv_zq = findViewById(R.id.tv_zq);
        tv_rzyq = findViewById(R.id.tv_rzyq);
        tv_fyjs = findViewById(R.id.tv_fyjs);

        ratingBar = findViewById(R.id.raingBar);

        tv_ckwz = findViewById(R.id.tv_wzck);
        tv_wz = findViewById(R.id.tv_wz);

        rv_zbts = findViewById(R.id.rv_fyxx_detail_zbts);
        rv_zbts.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));

   }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("房源详情");

        for (int i = 0; i < rvImgIds1.length; i++) {
            BasicTableItemBean item = new BasicTableItemBean(rvImgIds1[i], rvDatas1[i]);
            tableItemData1.add(i,item);
        }
        zbtsAdapter = new ZbtsRecyclerTabAdapter(tableItemData1);
        rv_zbts.setAdapter(zbtsAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogined = SPUtils.getInstance(this).getBoolean(SPConstants.IS_LOGIN,false);
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
                                    tv_wz.setText(fjDetailBeans.getXqName() + fjDetailBeans.getLdh() + "栋" + fjDetailBeans.getSh() + "室");

                                    tv_ld.setText(fjDetailBeans.getLdh() + "栋");
                                    tv_fjh.setText(fjDetailBeans.getSh() + "室");


//                                    ratingBar.setRating(Math.round(fjDetailBeans.getPf()));
                                    if ("1".equals(fjDetailBeans.getIssc())) {
                                        type = 1;
                                        iv_sc.setImageResource(R.mipmap.img_sc);
                                        tv_sc.setText("取消收藏");
                                    }else {
                                        type = 0;
                                        iv_sc.setImageResource(R.mipmap.img_wsc);
                                        tv_sc.setText("收藏");
                                    }




                                    if (!TextUtils.isEmpty(fjDetailBeans.getFjzp())) {

                                        String[] str1 = fjDetailBeans.getFjzp().split(",");
                                        imageInfoList.clear();

                                        for (int i = 0; i < str1.length; i++) {
                                            imageInfoList.add(new ImageInfo(OkHttpHelper.ROOT + str1[i]));
                                        }


                                        String[] titles = new String[imageInfoList.size()];
                                        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

                                        for (int i = 0; i < imageInfoList.size(); i++) {
                                            titles[i] = imageInfoList.get(i).getTitle();
                                            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(FyxxDetailActivity.this);
                                            simpleDraweeView.setAspectRatio(1.78f);
                                            // 设置一张默认的图片
                                            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(FyxxDetailActivity.this.getResources())
                                                    .setPlaceholderImage(ContextCompat.getDrawable(FyxxDetailActivity.this, R.mipmap.fjzp_mr), ScalingUtils.ScaleType.CENTER_CROP).build();
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
                                                    Intent intent = new Intent(FyxxDetailActivity.this, ImagePreviewActivity.class);
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


                                        adapter = new ImagesPagerAdapter(simpleDraweeViewList, mViewPager, FyxxDetailActivity.this);
                                        mViewPager.setAdapter(adapter);
                                        tv_dq_img.setText("1/" + imageInfoList.size() );
                                    }


                                    getPlList();
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FyxxDetailActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter1 = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter1);
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
//                Log.e(TAG, "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//                Toast.makeText(FyxxDetailActivity.this, "点击了回复", Toast.LENGTH_SHORT).show();
                showReplyEjDialog(groupPosition,childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }


    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getPlr() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {

                    dialog.dismiss();
//                    ReplyDetailBean detailBean = new ReplyDetailBean("小红", replyContent);
//                    adapter1.addTheReplyData(detailBean, position);
//                    expandableListView.expandGroup(position);
//                    Toast.makeText(FyxxDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    fbXxpl(replyContent,commentsList.get(position).getPlid(),commentsList.get(position).getPlr(),commentsList.get(position).getPlrid());
                } else {
                    Toast.makeText(FyxxDetailActivity.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyEjDialog(final int position,final  int childPosition) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getEjpl().get(childPosition).getPlr() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {

                    dialog.dismiss();
//                    ReplyDetailBean detailBean = new ReplyDetailBean("小红", replyContent);
//                    adapter1.addTheReplyData(detailBean, position);
//                    expandableListView.expandGroup(position);
//                    Toast.makeText(FyxxDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    fbXxpl(replyContent,commentsList.get(position).getPlid(),commentsList.get(position).getEjpl().get(childPosition).getPlr(),commentsList.get(position).getEjpl().get(childPosition).getPlrid());
                } else {
                    Toast.makeText(FyxxDetailActivity.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void getPlList(){
        HashMap<String, String> params = new HashMap<>();

        params.put("roomid", fjDetailBeans.getId() + "");
        params.put("csqLoginid", fjDetailBeans.getCsqLoginid());
        params.put("xqbh", fjDetailBeans.getXqbh());
        params.put("ldh", fjDetailBeans.getLdh() + "");
        params.put("sh", fjDetailBeans.getSh() + "");
        params.put("pageIndex", "1");
        params.put("pageSize", "100");



        OkHttpHelper.enqueue(OkHttpHelper.GET_PL_LIST, params, new OkHttpHelper.Callback() {
            @Override
            protected void onRequestSuccess(Call call, Response response) {//
                try {

                    String json = response.body().string();
                    final BaseBean<ArrayList<CommentDetailBean>> dataResult = new Gson().fromJson(json, new TypeToken<BaseBean<ArrayList<CommentDetailBean>>>() {
                    }.getType());
                    Log.i(TAG, "onRequestSuccess: " + json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (dataResult != null && dataResult.getCode() == 1) {//刷新界面
                                commentsList = dataResult.getData();
                                initExpandableListView(dataResult.getData());
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FyxxDetailActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }
    private void fbXxpl(String content,String pid,String bplr,String bplrid){
        HashMap<String, String> params = new HashMap<>();

        params.put("roomid", fjDetailBeans.getId() + "");
        params.put("csqLoginid", fjDetailBeans.getCsqLoginid());
        params.put("xqbh", fjDetailBeans.getXqbh());
        params.put("ldh", fjDetailBeans.getLdh() + "");
        params.put("sh", fjDetailBeans.getSh() + "");

        String id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String xm = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        String kind = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,"");
        params.put("plr", xm);
        params.put("plrid", id);
        params.put("nr", content);
        params.put("pid", pid);
        params.put("bplr", bplr);
        params.put("bplrid",bplrid);
        params.put("kind",kind);


        OkHttpHelper.enqueue(OkHttpHelper.FB_PL_NR, params, new OkHttpHelper.Callback() {
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
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
                                }

                                getPlList();
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FyxxDetailActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {

                    //commentOnWork(commentContent);
                    dialog.dismiss();
//                    CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent, "刚刚");
//                    adapter1.addTheCommentData(detailBean);
                    fbXxpl(commentContent,"0","","");

                } else {
                    Toast.makeText(FyxxDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void updateScStatusInfo(String  type){
        HashMap<String, String> params = new HashMap<>();
        params.put("roomid", fjDetailBeans.getId() + "");
        params.put("action", type);
        params.put("csqLoginid", fjDetailBeans.getCsqLoginid());
        params.put("xqbh", fjDetailBeans.getXqbh());
        params.put("ldh", fjDetailBeans.getLdh() + "");
        params.put("sh", fjDetailBeans.getSh() + "");
        String id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String xm = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        String kind = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,"");
        params.put("scrid", id);
        params.put("scr", xm);
        params.put("kind", kind);

        OkHttpHelper.enqueue(OkHttpHelper.UPDATE_SC_STATUS, params, new OkHttpHelper.Callback() {
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
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
                                }

                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FyxxDetailActivity.this, errorMsg);
                        }
                    }
                });
            }
        });
    }

    private void fyDf(){
        HashMap<String, String> params = new HashMap<>();
        params.put("roomid", fjDetailBeans.getId() + "");
        params.put("action", IntentConstants.ADD);
        params.put("csqLoginid", fjDetailBeans.getCsqLoginid());
        params.put("xqbh", fjDetailBeans.getXqbh());
        params.put("ldh", fjDetailBeans.getLdh() + "");
        params.put("sh", fjDetailBeans.getSh() + "");
        String id = SPUtils.getInstance(this).getString(SPConstants.LOGINID,"");
        String xm = SPUtils.getInstance(this).getString(SPConstants.USER_XM,"");
        String kind = SPUtils.getInstance(this).getString(SPConstants.USER_TYPE,"");
        params.put("pfrid", id);
        params.put("pfr", xm);
        params.put("kind", kind);
        params.put("fs", ratingBar.getRating() + "");

        OkHttpHelper.enqueue(OkHttpHelper.FY_DF, params, new OkHttpHelper.Callback() {
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
                            if (dataResult.getCode() == 1 && dataResult.getData() != null) {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                    ToastUtil.shortToast(FyxxDetailActivity.this, dataResult.getMsg());
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
                            ToastUtil.shortToast(FyxxDetailActivity.this, errorMsg);
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
                tv_dq_img.setText(((position) % (imageInfoList.size()) + 1) + "/" + imageInfoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        layout_lxfd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
                    @Override
                    public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                        super.onPermissionGranted(grantedPermissions);
                        if (!TextUtils.isEmpty(fjDetailBeans.getFdsjh())) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + fjDetailBeans.getFdsjh()));
                            startActivity(intent);
                        } else {
                            ToastUtil.shortToast(FyxxDetailActivity.this,"房东未登记手机号");
                        }
                    }
                }, PermissionHelper.Permission.CALL_PHONE);

            }
        });


        layout_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogined) {
                    switch (type) {
                        case 0:
                            updateScStatusInfo(IntentConstants.ADD);
                            iv_sc.setImageResource(R.mipmap.img_sc);
                            tv_sc.setText("取消收藏");
                            type = 1;
                            break;
                        case 1:
                            updateScStatusInfo(IntentConstants.EDIT);
                            iv_sc.setImageResource(R.mipmap.img_wsc);
                            tv_sc.setText("收藏");
                            type = 0;
                            break;
                    }

                } else {
                    ToastUtil.shortToast(FyxxDetailActivity.this,"请先登录");
                    startActivity(new Intent(FyxxDetailActivity.this,LoginActivity.class));
                }

            }
        });

        tv_ckwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FyxxDetailActivity.this, FastPathMapActivity.class);

                intent.putExtra("y", "31.359946");
                intent.putExtra("x", "120.956933");


                startActivity(intent);
            }
        });

        zbtsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(FyxxDetailActivity.this, PoiSearchActivity.class);
                intent.putExtra(IntentConstants.FJ_DATA, rvDatas1[position]);
                startActivity(intent);
            }
        });


        ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {// 打分
                if (isLogined) {
                    fyDf();
                } else {
                    ToastUtil.shortToast(FyxxDetailActivity.this,"请先登录，再评分");
                    startActivity(new Intent(FyxxDetailActivity.this,LoginActivity.class));
                }
            }
        });

        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined) {
                    showCommentDialog();
                } else {
                    ToastUtil.shortToast(FyxxDetailActivity.this,"请先登录，再评论");
                    startActivity(new Intent(FyxxDetailActivity.this,LoginActivity.class));
                }
            }
        });




    }

}
