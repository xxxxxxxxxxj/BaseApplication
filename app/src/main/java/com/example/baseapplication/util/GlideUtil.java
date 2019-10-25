package com.example.baseapplication.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.baseapplication.R;

/**
 * Glide图片加载工具类
 */
public class GlideUtil {
    public static void displayImage(Context mContext, String imgUrl,
                                    ImageView imageView) {
        if (StringUtil.isNotEmpty(imgUrl)) {
            if (imgUrl.toUpperCase().endsWith(".GIF")) {
                Glide.with(mContext)
                        .asGif()
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            }else{
                Glide.with(mContext)
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            }
        }
    }

    public static void displayImage(Context mContext, String imgUrl,
                                    ImageView imageView, int imgWidth, int imgHeight) {
        if (StringUtil.isNotEmpty(imgUrl)) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.override(imgWidth, imgHeight);
            if (imgUrl.toUpperCase().endsWith(".GIF")) {
                Glide.with(mContext)
                        .asGif()
                        .load(imgUrl)
                        .apply(requestOptions)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(requestOptions)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            }
        }
    }
}
