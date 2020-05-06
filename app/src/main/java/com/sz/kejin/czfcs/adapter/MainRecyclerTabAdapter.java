package com.sz.kejin.czfcs.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;


import java.util.List;

import androidx.annotation.Nullable;


public class MainRecyclerTabAdapter extends BaseQuickAdapter<BasicTableItemBean, BaseViewHolder> {

    public MainRecyclerTabAdapter(@Nullable List<BasicTableItemBean> data) {
        super(R.layout.main_rv_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BasicTableItemBean item) {
        helper.setText(R.id.mTv_tab_title,item.getTitle());
        helper.setImageResource(R.id.mIv_tab,item.getimageRes());
    }
}
