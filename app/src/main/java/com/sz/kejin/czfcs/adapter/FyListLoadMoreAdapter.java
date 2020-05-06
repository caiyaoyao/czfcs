package com.sz.kejin.czfcs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import androidx.annotation.Nullable;

public class FyListLoadMoreAdapter extends BaseQuickAdapter<MainFyXxListBeans, BaseViewHolder> implements LoadMoreModule {
    private Context mContext;

    public FyListLoadMoreAdapter(Context mContext) {
        super(R.layout.main_rv_fyxx_item);
        this.mContext = mContext;
    }


    public FyListLoadMoreAdapter(int layoutResId, @org.jetbrains.annotations.Nullable List<MainFyXxListBeans> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainFyXxListBeans item) {
        helper.setText(R.id.tv_main_fyxx_item_tv1,item.getJztype1() + " " + item.getXqName())
                .setText(R.id.tv_main_fyxx_item_tv2,item.getCsqName() + "|" + item.getMj() + "m²" + "|" + item.getHx() )
                .setText(R.id.tv_main_fyxx_item_tv3,item.getZj() + "元/月");
        ScaleRatingBar bar = helper.getView(R.id.raingBar);
        bar.setRating((Math.round(item.getPf())));


        helper.getView(R.id.iv_fyxx).setId(item.getId());

        if (!TextUtils.isEmpty(item.getFjzp()) && (item.getId() == helper.getView(R.id.iv_fyxx).getId())) {

            String[] str1 = item.getFjzp().split(",");
            if (str1.length > 0) {
                Glide.with(mContext).load(OkHttpHelper.ROOT + str1[0])
                        .override(240, 240)
                        .placeholder(R.drawable.white_corner_6_shape)
                        .error(R.mipmap.fjzp_mr)
                        .into((ImageView) helper.getView(R.id.iv_fyxx));
            }


        } else {
            Glide.with(mContext).load(R.mipmap.fjzp_mr).into((ImageView) helper.getView(R.id.iv_fyxx));
        }

    }
}
