package com.sz.kejin.czfcs.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.MainFyXxListBeans;
import com.sz.kejin.czfcs.bean.MyCommentBeans;
import com.sz.kejin.czfcs.helper.OkHttpHelper;

import java.util.List;

import androidx.annotation.Nullable;


public class WdCommentListAdapter extends BaseQuickAdapter<MyCommentBeans, BaseViewHolder> implements LoadMoreModule {
    private Context mContext;

    public WdCommentListAdapter(Context mContext) {
        super(R.layout.wd_comment_item);
        this.mContext = mContext;
    }

    public WdCommentListAdapter(@Nullable List<MyCommentBeans> data, Context context) {
        super(R.layout.wd_comment_item,data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCommentBeans item) {
        helper.setText(R.id.tv_wdpl_name,item.getPlr()).setText(R.id.tv_wdpl_time,item.getPosttime()).setText(R.id.tv_wdpl_reply,item.getNr());

        Glide.with(mContext).load(OkHttpHelper.ROOT + item.getTx())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.civ_wdpl_tx));
    }
}
