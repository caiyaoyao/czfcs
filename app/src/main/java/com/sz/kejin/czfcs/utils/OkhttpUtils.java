package com.sz.kejin.czfcs.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/8 0008.
 */

public class OkhttpUtils {
    private static OkhttpUtils okhttpUtils;
    private Handler handler;
    private final OkHttpClient httpClient;

    private OkhttpUtils(){
        handler=new Handler(Looper.getMainLooper());
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
    }

    public static OkhttpUtils getInstence(){
        if(okhttpUtils==null){
            synchronized (OkhttpUtils.class){
                if(okhttpUtils==null){
                    okhttpUtils=new OkhttpUtils();
                }
            }
        }

        return okhttpUtils;
    }


    private  void postJson(Object object, String url, Class clazz,
                           OnCallBackListener callBackListener, Map<String, String> map){
        try{
            JSONObject jsonObject = new JSONObject();
            if(map!=null&&!map.isEmpty()){
                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String value = next.getValue();
                    jsonObject.put(key,value);
                }

            }



            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString());
            Request request = new Request.Builder()
                    .tag(object)
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = getInstence().httpClient.newCall(request);
            setDataCall(clazz,call,callBackListener);
        }catch (Exception e){
            setError(callBackListener,e);
        }
    }


    private  void postJson_s(Object object, String url, Class clazz,
                             OnCallBackListener callBackListener, Map<String, String> map){
        try{
            JSONObject jsonObject = new JSONObject();
            if(map!=null&&!map.isEmpty()){
                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String value = next.getValue();
                    jsonObject.put(key,value);
                }

            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString());
            Request request = new Request.Builder()
                    .tag(object)
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = getInstence().httpClient.newCall(request);
            setDataCall_String(clazz,call,callBackListener);
        }catch (Exception e){
            setError(callBackListener,e);
        }
    }


    private void postForm(Object object, String url, Class clazz,
                          OnCallBackListener callBackListener, Map<String, String> map){
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null&&!map.isEmpty()){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                builder.add(key,value);
            }

        }

        Request request = new Request.Builder()
                .tag(object)
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstence().httpClient.newCall(request);
        setDataCall(clazz,call,callBackListener);
    }


    private void  postMutity(Object object, String url, Class clazz, Map<String, File> files,
                             OnCallBackListener callBackListener, Map<String, String> map){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String mesiaType=" application/octet-stream";
        if(map!=null&&!map.isEmpty()){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                builder.addFormDataPart(key,value);
            }

        }
        if(files!=null&&!files.isEmpty()){
            Set<Map.Entry<String, File>> entrySet = files.entrySet();
            Iterator<Map.Entry<String, File>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, File> next = iterator.next();
                String name = next.getKey();
                File file = next.getValue();
                if(file.getName().endsWith("jpg")){
                    mesiaType="image/jpeg";
                }else if(file.getName().endsWith("png")){
                    mesiaType="image/png";
                }else if(file.getName().endsWith("gif")){
                    mesiaType="image/gif";
                }
                RequestBody fileBody = RequestBody.create(MediaType.parse(mesiaType), file);
                Log.e("string", "name="+name );
                Log.e("string", "file.getName()="+file.getName() );
                builder.addFormDataPart(name,file.getName(),fileBody);
            }

        }
        Request request = new Request.Builder()
                .tag(object)
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstence().httpClient.newCall(request);
        setDataCall(clazz,call,callBackListener);
    }

    private void  postImage(Object object, String url, Class clazz, Map<String, File> files,
                            OnCallBackListener callBackListener, Map<String, String> map){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String mesiaType=" application/octet-stream";
        if(map!=null&&!map.isEmpty()){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                builder.addFormDataPart(key,value);
            }

        }
        if(files!=null&&!files.isEmpty()){
            Set<Map.Entry<String, File>> entrySet = files.entrySet();
            Iterator<Map.Entry<String, File>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, File> next = iterator.next();
                String name = next.getKey();
                File file = next.getValue();
                if(file.getName().endsWith("jpg")){
                    mesiaType="image/jpeg";
                }else if(file.getName().endsWith("png")){
                    mesiaType="image/png";
                }else if(file.getName().endsWith("gif")){
                    mesiaType="image/gif";
                }
                RequestBody fileBody = RequestBody.create(MediaType.parse(mesiaType), file);
                builder.addFormDataPart(name,file.getName(),fileBody);
            }

        }
        Request request = new Request.Builder()
                .tag(object)
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstence().httpClient.newCall(request);
        setDataCall(clazz,call,callBackListener);
    }


    private void fileDown(Object object, final String url, final String filePath,
                          final OnDownCallBackListener callBackListener){

        Request request = new Request.Builder()
                .tag(object)
                .url(url)
                .build();
        Call call = getInstence().httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setDownFileError(callBackListener,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                long lon = body.contentLength();
                InputStream inputStream = null;
                byte[] bytes = new byte[2048];
                int len=0;
                FileOutputStream fos=null;
                try{
                    inputStream = body.byteStream();

                    String nameFromUrl = getNameFromUrl(url);
                    Log.e("string", "nameFromUrl="+nameFromUrl );
                    File file = new File(filePath+"/"+nameFromUrl);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    fos=new FileOutputStream(file);
                    int sum=0;
                    while ((len=inputStream.read(bytes))!=-1){
                        fos.write(bytes,0,len);
                        sum+=len;
                        int prent= (int) (sum*1f/lon*100);
                        setDownFilePress(callBackListener,prent);
                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();
                    setDownFileSucess(callBackListener,"下载完成！",file);
                }catch (Exception e){

                    if(fos!=null){
                        fos.close();
                    }
                    if(inputStream!=null){
                        inputStream.close();
                    }
                    setDownFileError(callBackListener,e);
                }

            }
        });
    }
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    private  void setDataCall(final Class clazz, Call call, final OnCallBackListener callBackListener) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                setError(callBackListener,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("string", "string="+string);
                Gson gson = new Gson();
                Object fromJson=null;
                 try{
                     if(!TextUtils.isEmpty(string)&&string.length()>4){
                         String substring = string.substring(0, 4);
                         if(!substring.contains("d")){

                         }else {
                             Map<String, String> mapS = gson.fromJson(string, Map.class);
                             String d = mapS.get("d").toString();
                             if(!TextUtils.isEmpty(d)){
                                 string =d;
                                 if(string.substring(0,1).contains("[")){
                                     string="{\"list\":"+string+"}";
                                 }
                                 if(!string.contains("{")){
                                     string="{\"data\":"+string+"}";
                                 }
                             }else if(TextUtils.equals(string,"{\"d\":\"\"}")){
                                 string="{\"data\":"+"成功"+"}";
                             }
                         }
                     }else {
                         Map<String, String> mapS = gson.fromJson(string, Map.class);
                         String d = mapS.get("d").toString();
                         if(!TextUtils.isEmpty(d)){
                             string =d;
                             if(string.substring(0,1).contains("[")){
                                 string="{\"list\":"+string+"}";
                             }
                             if(!string.contains("{")){
                                 string="{\"data\":"+string+"}";
                             }
                         }else if(TextUtils.equals(string,"{\"d\":\"\"}")){
                             string="{\"data\":"+"成功"+"}";
                         }
                     }


                     Log.e("string", "string="+string);
                     fromJson = gson.fromJson(string, clazz);
                     if(fromJson!=null){
                       setSucess(callBackListener,fromJson);

                     }else {
                         Exception exception = new Exception("返回数据为空！");
                         setError(callBackListener,exception);
                     }

                 }catch (Exception e){
                     setError(callBackListener,e);
                 }

            }
        });
    }


    private  void setDataCall_String(final Class clazz, Call call,
                                     final OnCallBackListener callBackListener) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                setError(callBackListener,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("string", "string="+string);
                setSucess(callBackListener,string);
            }
        });
    }

    private void closeQuest(Object object){
        Dispatcher dispatcher = getInstence().httpClient.dispatcher();

        if(object!=null){
            List<Call> calls = dispatcher.queuedCalls();
            List<Call> runningCalls = dispatcher.runningCalls();
            for (int i = 0; i < calls.size(); i++) {
                Call call = calls.get(i);
                Object tag = call.request().tag();
                if(tag!=null&&object!=null){
                    if(object.equals(tag)){
                        call.cancel();
                    }
                }
            }
            for (int i = 0; i < runningCalls.size(); i++) {
                Call call = runningCalls.get(i);
                Object tag = call.request().tag();
                if(tag!=null&&object!=null){
                    if(object.equals(tag)){
                        call.cancel();
                    }
                }
            }
        }else {
            dispatcher.cancelAll();
        }

    }




    private  void setError(final OnCallBackListener callBackListener, final Exception e) {
        Log.e("string", "setError= "+e.toString() );
        getInstence().handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBackListener!=null){
                    callBackListener.onError(e);
                }
            }
        });
    }
    private  void setSucess(final OnCallBackListener callBackListener, final Object object) {
        getInstence().handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBackListener!=null){
                    callBackListener.onSucess(object);
                }

            }
        });
    }

    private  void setDownFileError(final OnDownCallBackListener callBackListener, final Exception e) {
        getInstence().handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBackListener!=null){
                    callBackListener.onError(e);
                }

            }
        });
    }
    private  void setDownFileSucess(final OnDownCallBackListener callBackListener, final String s, final File file) {
        getInstence().handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBackListener!=null){
                    callBackListener.onSucess(s,file);
                }

            }
        });
    }
    private  void setDownFilePress(final OnDownCallBackListener callBackListener, final int object) {
        getInstence().handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBackListener!=null){
                    callBackListener.onPress(object);
                }

            }
        });
    }

    public interface OnCallBackListener{
        void onSucess(Object object);
        void onError(Exception e);

    }
    public interface OnDownCallBackListener{
        void onSucess(String s, File f);
        void onError(Exception e);
        void onPress(int press);
    }


    public static void postString(Object object, String url, Class clazz,
                                  OnCallBackListener callBackListener, Map<String, String> map){
        getInstence().postJson(object,url,clazz,callBackListener,map);
    }
    public static void getString_S(Object object, String url, Class clazz,
                                   OnCallBackListener callBackListener, Map<String, String> map){
        getInstence().postJson_s(object,url,clazz,callBackListener,map);
    }
    public static void postFormString(Object object, String url, Class clazz,
                                      OnCallBackListener callBackListener, Map<String, String> map){
        getInstence().postForm(object,url,clazz,callBackListener,map);
    }
    public static void postFile(Object object, String url, Class clazz, Map<String, File> files,
                                OnCallBackListener callBackListener, Map<String, String> map){
        getInstence().postMutity(object,url,clazz,files,callBackListener,map);
    }
    public static void downFile(Object object, String url, String filePath,
                                OnDownCallBackListener callBackListener){
        getInstence().fileDown(object,url,filePath,callBackListener);
    }
    public static void close(Object object){
        getInstence().closeQuest(object);
    }


}
