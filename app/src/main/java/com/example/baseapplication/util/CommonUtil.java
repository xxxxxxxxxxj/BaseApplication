package com.example.baseapplication.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.baseapplication.R;
import com.example.baseapplication.app.AppConfig;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author：   zp
 * date：     2015/8/19 0019 17:45
 * <p/>       公共类,主要用于一些常用的方法
 * modify by  ljy
 */
public class CommonUtil {
    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    //查看大图
    public static void photoView(Activity context, ImageView imageView, RecyclerView recyclerView, int position, List<Object> imgList) {
        new XPopup.Builder(context).asImageViewer(imageView, position, imgList, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
            }
        }, new XPopupImageLoader() {
            @Override
            public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
                //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
                Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL)).placeholder(R.mipmap.ic_image_load).error(R.mipmap.ic_image_load).into(imageView);
            }

            @Override
            public File getImageFile(@NonNull Context context, @NonNull Object uri) {
                try {
                    return Glide.with(context).downloadOnly().load(uri).submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).show();
    }

    public static List<File> pathToFile(List<String> pathList) {
        List<File> fileList = new ArrayList<File>();
        for (int i = 0; i < pathList.size(); i++) {
            fileList.add(new File(pathList.get(i)));
        }
        return fileList;
    }

    public static List<String> fileToPath(List<File> fileList) {
        List<String> pathList = new ArrayList<String>();
        for (int i = 0; i < fileList.size(); i++) {
            pathList.add(fileList.get(i).getAbsolutePath());
        }
        return pathList;
    }

    /**
     * 判断当前日期是否在给定的两个日期之间
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static boolean compareDateState(String beginDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c.setTimeInMillis(System.currentTimeMillis());
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endDate));
        } catch (java.text.ParseException e) {
            return false;
        }
        int resultBegin = c.compareTo(c1);
        int resultEnd = c.compareTo(c2);
        return resultBegin > 0 && resultEnd < 0;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context,
                                             final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static Uri getUri(Context mContext, File file) {
        Uri mUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", file);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        return mUri;
    }

    public static File createFile(Context mContext, int flag, String versionName) throws IOException {
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
                tempFile = createImageFile(mContext, true, true, AppConfig.FILENAME_DEVICEID, AppConfig.DIRECTORY_DEVICEID);
                break;
            case 5://下载的apk存储的文件夹
                tempFile = createApkFile(mContext, versionName);
                break;
            case 6://拍摄视频存储的文件夹
                tempFile = createVideoFile(mContext, true, false, "", AppConfig.DIRECTORY_VIDEO);
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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡在手机上是否是正常使用状态
            //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
            if (isPublic) {
                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                if (!storageDir.exists()) storageDir.mkdirs();
            } else {
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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡在手机上是否是正常使用状态
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

    public static File createVideoFile(Context mContext, boolean isPublic, boolean isHaveFileName, String fileName, String directory) throws IOException {
        // Create an image file name
        File tempFile = null;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = String.format("JPEG_%s.mp4", timeStamp);
        File storageDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡在手机上是否是正常使用状态
            //external storage外部存储,路径是:SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES);
            if (!storageDir.exists()) storageDir.mkdirs();
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
}
