package com.example.baseapplication.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.baseapplication.R;
import com.example.baseapplication.app.UrlConstants;

/**
 * Glide图片加载工具类
 */
public class GlideUtil {
    public static void displayImage(Context mContext, String imgUrl,
                                    ImageView imageView) {
        if (StringUtil.isNotEmpty(imgUrl)) {
            if (!imgUrl.startsWith("http://")
                    && !imgUrl.startsWith("https://")) {
                imgUrl = UrlConstants.getServiceBaseUrl() + imgUrl;
            }
            Glide.with(mContext)
                    .load(imgUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    }
}
