package com.sz.kejin.czfcs.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * v1.0.0 - note - 2018/7/18
 */

public class ToastUtil {

    private static Toast mToast;

    public static void shortToast(Context context, String msg){
        if(!TextUtils.isEmpty(msg)){
            try {
                if (mToast == null) {
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                }
                mToast.setText(msg);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void shortToast(Context context, int msgId){
        String msg = context.getString(msgId);
        if(!TextUtils.isEmpty(msg)){
            if(mToast == null){
                mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
