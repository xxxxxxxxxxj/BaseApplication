package com.example.baseapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.baseapplication.app.AppConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/25
 * 描    述：
 * =====================================
 */
public class FileUtil {
    /**
     * 判断外部存储是否可用
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static File createFile(Context mContext, int flag, String versionName, Bitmap bitmap) throws IOException {
        File tempFile = null;
        switch (flag) {
            case 1://拍照存储图片的文件夹
                tempFile = createImageFile(mContext, true, true, "", AppConfig.DIRECTORY_CAPTURE);
                break;
            case 2://裁剪存储图片的文件夹
                tempFile = createImageFile(mContext, true, true, "", AppConfig.DIRECTORY_CROP);
                break;
            case 3://鲁班压缩存储图片的文件夹
                tempFile = createImageFile(mContext, true, false, "", AppConfig.DIRECTORY_LUBAN);
                break;
            case 4://设备唯一ID存储的文件夹
                tempFile = createDeviceIdFile(mContext);
                break;
            case 5://下载的apk存储的文件夹
                tempFile = createApkFile(mContext, versionName);
                break;
            case 6://拍摄视频存储的文件夹
                tempFile = createVideoFile(mContext, true, false, "", AppConfig.DIRECTORY_VIDEO);
                break;
            case 7://拍摄视频生成封面以及拍照存储的文件夹
                tempFile = createImgBitmapFile(mContext, true, true, "", bitmap, AppConfig.DIRECTORY_VIDEO_FRAME);
                break;
        }
        return tempFile;
    }

    public static File createImageFile(Context mContext, boolean isPublic, boolean isHaveFileName, String fileName, String directory) throws IOException {
        // Create an image file name
        File tempFile = null;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir;
        if (externalMemoryAvailable()) {//判断sd卡在手机上是否是正常使用状态
            if (isPublic) {
                //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                if (!storageDir.exists()) storageDir.mkdirs();
            } else {
                //external storage外部存储,路径为:/mnt/sdcard/Android/data/< package name >/files/…
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
        } else {
            //internal storage内部存储,路径是:/data/data/< package name >/files/…
            storageDir = mContext.getFilesDir();
        }
        if (StringUtil.isNotEmpty(directory)) {
            storageDir = new File(storageDir, directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        }
        // Avoid joining path components manually
        if (isHaveFileName) {
            if (StringUtil.isNotEmpty(fileName)) {
                tempFile = new File(storageDir, fileName);
            } else {
                tempFile = new File(storageDir, imageFileName);
            }
        } else {
            tempFile = storageDir;
        }
        return tempFile;
    }

    public static File createApkFile(Context mContext, String versionName) throws IOException {
        String fileName = mContext.getPackageName() + "_" + versionName + ".apk";
        File tempFile = null;
        if (externalMemoryAvailable()) {//判断sd卡在手机上是否是正常使用状态
            //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
            tempFile = Environment.getExternalStorageDirectory();
        } else {
            //internal storage内部存储,路径是:/data/data/< package name >/files/…
            tempFile = mContext.getFilesDir();
        }
        tempFile = new File(tempFile, AppConfig.DIRECTORY_APK);
        if (!tempFile.exists()) tempFile.mkdirs();
        tempFile = new File(tempFile, fileName);
        return tempFile;
    }

    public static File createDeviceIdFile(Context mContext) throws IOException {
        File tempFile = null;
        if (externalMemoryAvailable()) {//判断sd卡在手机上是否是正常使用状态
            //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
            tempFile = Environment.getExternalStorageDirectory();
        } else {
            //internal storage内部存储,路径是:/data/data/< package name >/files/…
            tempFile = mContext.getFilesDir();
        }
        tempFile = new File(tempFile, AppConfig.DIRECTORY_DEVICEID);
        if (!tempFile.exists()) tempFile.mkdirs();
        tempFile = new File(tempFile, AppConfig.FILENAME_DEVICEID);
        return tempFile;
    }

    public static File createVideoFile(Context mContext, boolean isPublic, boolean isHaveFileName, String fileName, String directory) throws IOException {
        // Create an image file name
        File tempFile = null;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = String.format("JPEG_%s.mp4", timeStamp);
        File storageDir;
        if (externalMemoryAvailable()) {//判断sd卡在手机上是否是正常使用状态
            if (isPublic) {
                //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES);
                if (!storageDir.exists()) storageDir.mkdirs();
            } else {
                //external storage外部存储,路径为:/mnt/sdcard/Android/data/< package name >/files/…
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            }
        } else {
            //internal storage内部存储,路径是:/data/data/< package name >/files/…
            storageDir = mContext.getFilesDir();
        }
        if (StringUtil.isNotEmpty(directory)) {
            storageDir = new File(storageDir, directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        }
        // Avoid joining path components manually
        if (isHaveFileName) {
            if (StringUtil.isNotEmpty(fileName)) {
                tempFile = new File(storageDir, fileName);
            } else {
                tempFile = new File(storageDir, videoFileName);
            }
        } else {
            tempFile = storageDir;
        }
        return tempFile;
    }

    public static File createImgBitmapFile(Context mContext, boolean isPublic, boolean isHaveFileName, String fileName, Bitmap bitmap, String directory) throws IOException {
        File tempFile = null;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir;
        if (externalMemoryAvailable()) {//判断sd卡在手机上是否是正常使用状态
            if (isPublic) {
                //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                if (!storageDir.exists()) storageDir.mkdirs();
            } else {
                //external storage外部存储,路径为:/mnt/sdcard/Android/data/< package name >/files/…
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
        } else {
            //internal storage内部存储,路径是:/data/data/< package name >/files/…
            storageDir = mContext.getFilesDir();
        }
        if (StringUtil.isNotEmpty(directory)) {
            storageDir = new File(storageDir, directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        }
        // Avoid joining path components manually
        if (isHaveFileName) {
            if (StringUtil.isNotEmpty(fileName)) {
                tempFile = new File(storageDir, fileName);
            } else {
                tempFile = new File(storageDir, imageFileName);
            }
        } else {
            tempFile = storageDir;
        }
        FileOutputStream fout = new FileOutputStream(tempFile.getAbsolutePath());
        BufferedOutputStream bos = new BufferedOutputStream(fout);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return tempFile;
    }
}
