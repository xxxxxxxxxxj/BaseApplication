package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.ImageInfo;
import com.example.baseapplication.mvp.view.widget.NiceImageView;
import com.example.baseapplication.util.CommonUtil;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.QMUIDisplayHelper;

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
        int windowWidth = QMUIDisplayHelper.getScreenWidth(mContext);
        int imgWidth = (windowWidth - QMUIDisplayHelper.dp2px(mContext, (int) offSet)) / 3;
        int imgHeight = imgWidth;
        ViewGroup.LayoutParams layoutParams = iv_item_img.getLayoutParams();
        layoutParams.width = imgWidth;
        layoutParams.height = imgHeight;
        iv_item_img.setLayoutParams(layoutParams);
        GlideUtil.displayImage(mContext, item, iv_item_img);
        iv_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.photoView(mActivity, helper.getLayoutPosition(), imgList);
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