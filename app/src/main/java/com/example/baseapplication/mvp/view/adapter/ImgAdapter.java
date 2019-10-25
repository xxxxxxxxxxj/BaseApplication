package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.ImageInfo;
import com.example.baseapplication.mvp.view.widget.NiceImageView;
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
    private final float offSet;
    private Activity mActivity;
    private List<ImageInfo> imgList = new ArrayList<ImageInfo>();

    public ImgAdapter(Activity mActivity, int layoutResId, List<String> data, float offSet) {
        super(layoutResId, data);
        this.mActivity = mActivity;
        this.offSet = offSet;
        imgList.clear();
        for (int i = 0; i < mData.size(); i++) {
            imgList.add(new ImageInfo(mData.get(i)));
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        NiceImageView iv_item_img = helper.getView(R.id.iv_item_img);
        int windowWidth = ScreenUtil.getScreenWidth(mContext);
        int imgWidth = (windowWidth - DensityUtil.dp2px(mContext, offSet)) / 3;
        int imgHeight = imgWidth;
        GlideUtil.displayImage(mContext, item, iv_item_img, imgWidth, imgHeight);
        iv_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.photoView(mActivity, helper.getLayoutPosition(), imgList);
            }
        });
    }

    public void setImgData(List<String> data) {
        imgList.clear();
        for (int i = 0; i < data.size(); i++) {
            imgList.add(new ImageInfo(data.get(i)));
        }
        notifyDataSetChanged();
    }
}