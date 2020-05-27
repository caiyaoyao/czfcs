package com.sz.kejin.czfcs.adapter;


import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;


import java.util.List;


public class FjlbLdhRecyclerTabAdapter extends BaseQuickAdapter<BasicTableItemBean, BaseViewHolder> {

    public FjlbLdhRecyclerTabAdapter(@Nullable List<BasicTableItemBean> data) {
        super(R.layout.fjgl_fjlb_ldh_rv_item,data);
    }

    private Context mContext;

    public FjlbLdhRecyclerTabAdapter(@Nullable List<BasicTableItemBean> data, Context context) {
        super(R.layout.fjgl_fjlb_ldh_rv_item,data);
        this.mContext = context;
    }



    @Override
    protected void convert(BaseViewHolder helper, BasicTableItemBean item) {
        helper.setText(R.id.mTv_tab_title,item.getTitle());
        if (item.getimageRes() == 0) {
            helper.getView(R.id.mTv_tab_title).setSelected(true);
        } else {
            helper.getView(R.id.mTv_tab_title).setSelected(false);
        }
    }
}
