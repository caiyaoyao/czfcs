package com.sz.kejin.czfcs.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;

import java.util.List;


public class ZbtsRecyclerTabAdapter extends BaseQuickAdapter<BasicTableItemBean, BaseViewHolder> {

    public ZbtsRecyclerTabAdapter(@Nullable List<BasicTableItemBean> data) {
        super(R.layout.zbts_rv_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BasicTableItemBean item) {
        helper.setText(R.id.mTv_tab_title,item.getTitle());
        helper.setImageResource(R.id.mIv_tab,item.getimageRes());
    }
}
