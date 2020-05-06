package com.sz.kejin.czfcs.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.activity.BjZhInfoActivity;
import com.sz.kejin.czfcs.activity.ZhListActivity;
import com.sz.kejin.czfcs.bean.BaseBean;
import com.sz.kejin.czfcs.bean.FwhcXxBeans;
import com.sz.kejin.czfcs.constant.IntentConstants;
import com.sz.kejin.czfcs.helper.OkHttpHelper;
import com.sz.kejin.czfcs.utils.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;

public class FwhcAdapter extends RecyclerView.Adapter<FwhcAdapter.RecordHolder> {
    private static final String TAG = "FwhcAdapter";

    private Activity mContext;
    private ArrayList<FwhcXxBeans.ZhxxBean> beans;
    private FwhcXxBeans.FjxxBean fjxxBeans;

    public FwhcAdapter(Activity mContext, ArrayList<FwhcXxBeans.ZhxxBean> beans, FwhcXxBeans.FjxxBean fjxxBeans) {
        this.mContext = mContext;
        this.beans = beans;
        this.fjxxBeans = fjxxBeans;
    }

    public FwhcAdapter() {
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.fwhc_item, parent, false);
        return new RecordHolder(itemView);
    }

    private void deleteZhInfo(String zhid){

            HashMap<String, String> params = new HashMap<>();
            params.put("zhid", zhid);

            OkHttpHelper.enqueue(OkHttpHelper.DELETE_ZH_INFO, params, new OkHttpHelper.Callback() {
                @Override
                protected void onRequestSuccess(Call call, Response response) {
                    try {

                        String json = response.body().string();
                        final BaseBean dataResult = new Gson().fromJson(json, new TypeToken<BaseBean>() {
                        }.getType());
                        Log.i(TAG, "onRequestSuccess: " + json);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dataResult != null && dataResult.getCode() == 1) {
                                    if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                        ToastUtil.shortToast(mContext, dataResult.getMsg());
                                    }
                                    if (mContext instanceof ZhListActivity) {
                                       ((ZhListActivity) mContext).getFjInfoById();
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(dataResult.getMsg())) {
                                        ToastUtil.shortToast(mContext, dataResult.getMsg());
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
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(errorMsg)) {
                                ToastUtil.shortToast(mContext, errorMsg);
                            }
                        }
                    });
                }
            });
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        FwhcXxBeans.ZhxxBean bean = beans.get(position);
        holder.tv_xm.setText(bean.getXm());
        holder.tv_xb.setText(bean.getXb());
        holder.tv_sjh.setText(bean.getLxdh());
        holder.tv_sfzh.setText(bean.getSfzh());
        holder.tv_zkxx.setText(bean.getSrc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });

        holder.tv_xgxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BjZhInfoActivity.class);
//                ZhTzBeans zhTzBeans = new ZhTzBeans(beans.get(position).getId(), "edit", beans.get(position).getSrc(),beans.get(position).getRoomid() + "");
                FwhcXxBeans.FjxxBean zhxxBean = fjxxBeans;
//                zhxxBean.setType("edit");
//                zhxxBean.setSrc(bean.getSrc());
//                intent.putExtra(IntentConstants.ZH_DATA_Detail, zhxxBean);
//                intent.putExtra("zhid", bean.getId());

                intent.putExtra(IntentConstants.EDIT_OR_ADD_TYPE, IntentConstants.EDIT);
                intent.putExtra(IntentConstants.FJ_DATA, beans.get(position).getRoomid());
                intent.putExtra(IntentConstants.ZH_DATA, bean.getId());
                mContext.startActivity(intent);
            }
        });

        holder.tv_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                new AlertView("提示", "确定删除当前用户?", "取消", new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                deleteZhInfo(bean.getId() + "");
                                break;
                        }
                    }
                }).show();
//                deleteZhInfo(bean.getId() + "");
            }
        });

   }




    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }



    class RecordHolder extends RecyclerView.ViewHolder{
        private  TextView tv_xm,tv_xb,tv_sjh,tv_sfzh,tv_xgxx,tv_sc,tv_zkxx;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            tv_xm = itemView.findViewById(R.id.tv_fwhc_item_xm);
            tv_xb = itemView.findViewById(R.id.tv_fwhc_item_xb);
            tv_sjh = itemView.findViewById(R.id.tv_fwhc_item_sjh);
            tv_sfzh = itemView.findViewById(R.id.tv_fwhc_item_sfzh);
            tv_xgxx = itemView.findViewById(R.id.tv_xgxx);
            tv_sc = itemView.findViewById(R.id.tv_sc);
            tv_zkxx = itemView.findViewById(R.id.tv_fwhc_item_zkxx);

        }
    }

    //私有属性
    private OnItemClickListener onItemClickListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }


}
