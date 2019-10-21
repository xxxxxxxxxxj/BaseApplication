package com.example.baseapplication.mvp.view.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baseapplication.mvp.model.entity.ImageInfo;
import com.example.baseapplication.photoview.PhotoView;
import com.example.baseapplication.util.GlideUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:查看大图适配器</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-12 20:14
 */
public class PhotoViewPagerAdapter extends PagerAdapter {
    private List<ImageInfo> imgList;
    private Context context;

    public PhotoViewPagerAdapter(Context context, List<ImageInfo> imgList) {
        this.imgList = imgList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    //指定复用的判断逻辑，固定写法：view == object
    @Override
    public boolean isViewFromObject(View view, Object object) {
        //当创建新的条目，又反回来，判断view是否可以被复用(即是否存在)
        return view == object;
    }

    //返回要显示的条目内容
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        GlideUtil.displayImage(context, imgList.get(position).getImgUrl(), photoView);
        //把图片添加到container中
        container.addView(photoView);
        //把图片返回给框架，用来缓存
        return photoView;
    }

    //销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //object:刚才创建的对象，即要销毁的对象
        container.removeView((View) object);
    }
}
