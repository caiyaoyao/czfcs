package com.sz.kejin.czfcs.manager;

import android.content.Context;
import android.os.Environment;

import com.allenliu.versionchecklib.utils.FileHelper;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.sz.kejin.czfcs.bean.UpdateAppBean;

import java.io.File;


public class UpdateManager {

    private static UpdateManager instance = new UpdateManager();
    private DownloadBuilder builder;
    private UpdateAppForceListen updateAppForceListen;

    public static UpdateManager getInstance() {
        return instance;
    }

    public void sendRequest(final Context context, final UpdateAppBean updateAppBean) {
        builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(crateUIData(context, updateAppBean))
                .setDownloadAPKPath(FileHelper.checkSDCard() ?
                        Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/"
                        : Environment.getDataDirectory().getPath() + "/" + context.getPackageName() + "/");

        try {
            File downFile = new File(builder.getDownloadAPKPath());
            if (downFile.exists() && downFile.isDirectory()) {
                File[] files = downFile.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        if (updateAppBean.getForce_update().equals(UpdateAppBean.FORCE_UPDATE)) {
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    if (updateAppForceListen != null) {
                        updateAppForceListen.onShouldForceUpdate();
                    }
                }
            });
        }

        builder.setForceRedownload(true);
        builder.executeMission(context);
    }

    private UIData crateUIData(Context context, UpdateAppBean updateAppBean) {
        UIData uiData = UIData.create();
        uiData.setTitle("软件升级");
        uiData.setDownloadUrl(updateAppBean.getUrl());
        uiData.setContent(updateAppBean.getContent());
        return uiData;
    }

    public void setUpdateAppForceListen(UpdateAppForceListen updateAppForceListen) {
        this.updateAppForceListen = updateAppForceListen;
    }


    public interface UpdateAppForceListen {
        void onShouldForceUpdate();
    }
}
