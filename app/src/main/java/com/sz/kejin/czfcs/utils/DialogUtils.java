package com.sz.kejin.czfcs.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.view.LoadSuessView;


/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class DialogUtils {

    //加载中...
    public static KProgressHUD showkProgress(Activity activity, String data, int time, boolean cancel) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(R.drawable.suprise);

        KProgressHUD kProgressHUD = KProgressHUD.create(activity);
        kProgressHUD.setLabel(data);
        kProgressHUD.setCancellable(cancel);
        kProgressHUD.setDimAmount(0.5f);
        kProgressHUD.setCustomView(imageView);
        kProgressHUD.show();
        scheduleDismiss(kProgressHUD,time);
        return kProgressHUD;
    }


    /**
     * 定时销毁
     */
    public static  void scheduleDismiss(final KProgressHUD kProgressHUD, int time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (kProgressHUD != null && kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        }, time);
    }


    public static Dialog showLoading(Activity activity, String st, int type) {
        //type:1加载，2成功，3失败
        Dialog dialog = new Dialog(activity, R.style.dialog);
        View inflate = View.inflate(activity, R.layout.load_dia_layout, null);
        LoadSuessView loadSuessView = (LoadSuessView) inflate.findViewById(R.id.lsv_load);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_load);
        ImageView ivAnim= (ImageView) inflate.findViewById(R.id.iv_loa);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.rotate_anim);
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.start();
        ivAnim.setAnimation(animation);
        if (type == 1) {
            ivAnim.setVisibility(View.VISIBLE);
            loadSuessView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        } else if (type == 2) {
            loadSuessView.setVisibility(View.VISIBLE);
            ivAnim.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        } else if (type == 3) {
            imageView.setVisibility(View.VISIBLE);
            ivAnim.setVisibility(View.GONE);
            loadSuessView.setVisibility(View.GONE);
        }
        TextView tvNr = (TextView) inflate.findViewById(R.id.tv_load);
        tvNr.setText(st);
        dialog.setContentView(inflate);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.4); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.45); //宽度设置为屏幕的0.5
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(true);
        dialog.getWindow().setAttributes(p);  //设置生效
        return dialog;
    }

    public static void showNoticeDia(final Activity activity, String st) {
        View inflate = View.inflate(activity, R.layout.notice_dia_layout, null);
        TextView tvNr = (TextView) inflate.findViewById(R.id.tv_txnr);
        tvNr.setText(st);
        final Dialog dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(inflate);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.8); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.45); //宽度设置为屏幕的0.5
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(true);
        dialog.getWindow().setAttributes(p);  //设置生效
        inflate.findViewById(R.id.btn_tx_dia_jgl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                activity.finish();
            }
        });
        inflate.findViewById(R.id.btn_tx_dia_zdl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    public static void showNoticeDia2(final Activity activity, String st) {
        View inflate = View.inflate(activity, R.layout.notice_dia_layout, null);
        LinearLayout layoutSF = (LinearLayout) inflate.findViewById(R.id.layout_text_dia_sf);
        LinearLayout layoutJY = (LinearLayout) inflate.findViewById(R.id.layout_text_dia_jy);
        layoutJY.setVisibility(View.GONE);
        layoutSF.setVisibility(View.GONE);
        TextView tvDia = (TextView) inflate.findViewById(R.id.text_dia_new);
        tvDia.setText(st);
        final Dialog dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(inflate);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.8); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.45); //宽度设置为屏幕的0.5
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(true);
        dialog.getWindow().setAttributes(p);  //设置生效
        inflate.findViewById(R.id.btn_tx_dia_jgl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                activity.finish();
            }
        });
        inflate.findViewById(R.id.btn_tx_dia_zdl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

}
