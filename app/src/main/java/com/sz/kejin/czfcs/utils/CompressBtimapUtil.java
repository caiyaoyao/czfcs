package com.sz.kejin.czfcs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompressBtimapUtil {

    /**
     * 压缩图片
     * @param bitmap
     * @return
     */
    public static void compressBitmap(final Bitmap bitmap, final String path, final OnImgPressListener imgPressListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                long start = System.currentTimeMillis(), end;

                File file = null;

                if (bitmap != null) {

                    //先压缩尺寸
                    Bitmap currentBitmap = ratio(path, 1080, 1920);

                    //再压缩质量
                    int quality = 100;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    currentBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    while (baos.toByteArray().length / 1024 > 0.3 * 1024) {
                        if (11 > quality) break;
                        else quality -= 10;

                        baos.reset();

                        currentBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        Log.i("compressBitmap", "compressBitmap: size - " + baos.toByteArray().length);
                    }

                    file = new File(path);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        try {
                            fos.write(baos.toByteArray());
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    close(baos);
                }

                end = System.currentTimeMillis();
                Log.d("compressBitmap", "compressBitmap: cost - " + (end - start) / 1000);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                final File finalFile = file;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //已在主线程中，可以更新UI
                        imgPressListener.imgPressListener(finalFile);
                    }
                });
            }
        }).start();

    }

    public interface OnImgPressListener{
        void imgPressListener(File file);
    }

    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w >= h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;


    }



    /**
     * 获取合适的采样率
     * @param opt
     * @param mImgWidth
     * @param mImgHeight
     * @return
     */
    private static int calSampleSize(BitmapFactory.Options opt, int mImgWidth, int mImgHeight) {
        int outWidth = opt.outWidth;
        int outHeight = opt.outHeight;
        int radio_w = 1, radio_h = 1;
        int sampleSize = 1;
        if (outWidth > mImgWidth || outHeight > mImgHeight) {
            radio_w = outWidth / mImgWidth;
            radio_h = outWidth / mImgHeight;
            sampleSize = Math.min(radio_w, radio_h);
        }
        Log.i("calSampleSize", "sampleSize: " + sampleSize);
        return sampleSize;
    }

    /**
     * 关闭流
     * @param cList
     */
    private static void close(Closeable... cList) {
        for (Closeable c : cList) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
