package com.sz.kejin.czfcs.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.sz.kejin.czfcs.application.MyApplication;

import java.util.List;


/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class ToolUtil {
    private static final String TAG = "ToolUtil";

    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    //复制到剪切板
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboard(String szContent) {
        Context context = MyApplication.getInstance();

        String sourceText = szContent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(sourceText);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
        }
    }

    //复制到剪切板
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboardAndToast(Context context, String szContent) {

        String sourceText = szContent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(sourceText);
            Toast.makeText(context, "copy success", Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "copy success", Toast.LENGTH_SHORT).show();
        }
    }

    //判断某个应用是否安装
    public static boolean checkAppInstalled( Context context, String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if(info == null || info.isEmpty())
            return false;
        for ( int i = 0; i < info.size(); i++ ) {
            if(pkgName.equals(info.get(i).packageName)) {
                Log.i(TAG, "checkAppInstalled: 1111111111111" + info.get(i).packageName);
                return true;

            }
        }
        return false;
    }

    public static boolean checkAppInstalled2(Context context,String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }

}
