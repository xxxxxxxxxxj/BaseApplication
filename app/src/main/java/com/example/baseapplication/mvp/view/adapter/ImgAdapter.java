package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.ImageInfo;
import com.example.baseapplication.util.DensityUtil;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.ScreenUtil;
import com.example.baseapplication.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-23 12:50
 */
public class ImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Activity mActivity;
    private List<String> pathList = new ArrayList<String>();
    private int imgWidth;
    private int imgHeight;
    private List<ImageInfo> imgList = new ArrayList<ImageInfo>();

    public ImgAdapter(Activity mActivity, int layoutResId, List<String> data, int imgWidth, int imgHeight) {
        super(layoutResId, data);
        this.mActivity = mActivity;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        pathList.clear();
        imgList.clear();
        for (int i = 0; i < mData.size(); i++) {
            pathList.add(mData.get(i));
            imgList.add(new ImageInfo(mData.get(i)));
        }
    }

    public ImgAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
        pathList.clear();
        for (int i = 0; i < mData.size(); i++) {
            pathList.add(mData.get(i));
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        ImageView iv_item_img = helper.getView(R.id.iv_item_img);
        if (imgWidth > 0 && imgHeight > 0) {
            int windowWidth = ScreenUtil.getScreenWidth(mContext);
            int localImgWidth = (windowWidth - DensityUtil.dp2px(mContext, 70)) / 3;
            int localImgHeight = localImgWidth * imgHeight / imgWidth;
            GlideUtil.displayImage(mContext, item, iv_item_img, localImgWidth, localImgHeight);
        } else {
            GlideUtil.displayImage(mContext, item, iv_item_img);
        }
        iv_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.photoView(mActivity, helper.getLayoutPosition(), imgList, v);
            }
        });
    }
}