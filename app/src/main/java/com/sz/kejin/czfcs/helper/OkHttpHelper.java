package com.sz.kejin.czfcs.helper;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class OkHttpHelper {


    private static final String TAG = "OkHttpHelper";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static final OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .addInterceptor(new NetInterceptor())
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    public static String KF_URL = "http://192.168.1.200:8019/";
    public static String FB_URL = "http://202.102.43.44:8001/";
//    public static String ROOT = KF_URL;
    public static String ROOT = FB_URL;

    public static String UPLOAD_URL = ROOT + "PubService/Czfcs/upload.aspx" ;
    public static String MAIN_URL = "PubService/Czfcs/WebService.asmx/" ;

    public static final String CHECK_VERSION ="bbsj";
    public static final String LOGIN ="login";
    public static final String USER_REGISTER ="zc";
    public static final String GET_YZM ="sendyzm" ;
    public static final String CHANGE_PASSWORD ="" ;
    public static final String GET_SQ ="getsq" ;
    public static final String GET_XQ_LIST ="getxq" ;
    public static final String GET_LDH_LIST ="getldh" ;
    public static final String GET_FJH_LIST ="getsh" ;
    public static final String GET_SFZ_INFO ="idCardHelper" ;
    public static final String GET_SF_LIST = "bindhjd_s";
    public static final String GET_S_LIST = "bindhjd_city";
    public static final String GET_QX_LIST = "bindhjd_qx";
    public static final String GET_FWHC_INFO_BY_ID ="getqtxx" ;
    public static final String DELETE_ZH_INFO = "delzhxx";
    public static final String GET_FWHC_ZH_INFO_BY_ID ="getzhxx";
    public static final String EDIT_ZH_INFO = "editzhxx";
    public static final String FB_FY_INFO = "lrfyxx";
    public static final String GET_MAIN_FY_LIST = "TopFjxx";
    public static final String GET_FY_LIST = "GetFyList";
    public static final String GET_FY_INFO_DETAIL = "GetFjxx";
    public static final String GET_WDFY_LIST = "GetMyFyList";
    public static final String UPDATE_SC_STATUS = "xxsc";
    public static final String GET_GR_XX = "getxx";
    public static final String XG_GR_XX = "xgxx";
    public static final String XG_GR_TX = "xgtx";
    public static final String GET_CZ_LX_LIST = "getczlx";
    public static final String GET_YZJ_LIST = "getyzj";
    public static final String GET_HX_LIST = "gethx";
    public static final String FB_PL_NR = "xxpl";
    public static final String GET_PL_LIST = "pllb";
    public static final String FY_DF = "xxpf";
    public static final String WD_SC_LIST = "sclb";
    public static final String WD_PL_LIST = "mypllb";



    public static void enqueue(String url, Map<String, String> params, Callback callback) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }

        }
        Request request = new Request.Builder().url(ROOT + MAIN_URL + url).post(bodyBuilder.build()).build();
        Log.i(TAG, "enqueue: url:" + ROOT  + MAIN_URL +  url);
        client.newCall(request).enqueue(callback);
    }

    public static void uploadImg(String url, Map<String, String> params, File file, Callback callback) {
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        builder.addFormDataPart("file", file.getName(), fileBody);
        Request request = new Request.Builder().url(ROOT +  url).post(builder.build()).build();
        client.newCall(request).enqueue(callback);

    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder().url( ROOT + url).build();
        client.newCall(request).enqueue(callback);
    }

    public static abstract class Callback implements okhttp3.Callback {

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                onRequestSuccess(call, response);
            } else {
                onRequestFail(call, response.message());
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            onRequestFail(call, e.getMessage());
        }

        protected abstract void onRequestSuccess(Call call, Response response);

        protected abstract void onRequestFail(Call call, String errorMsg);
    }

}
