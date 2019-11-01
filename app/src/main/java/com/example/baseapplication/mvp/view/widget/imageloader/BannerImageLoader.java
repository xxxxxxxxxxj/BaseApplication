package com.example.baseapplication.mvp.view.widget.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.baseapplication.R;
import com.youth.banner.loader.ImageLoader;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-23 11:36
 */
public class BannerImageLoader extends ImageLoader {

    public BannerImageLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context)
                .load(path).error(R.mipmap.ic_image_load)
                .placeholder(R.mipmap.ic_image_load)
                .into(imageView);
    }
}