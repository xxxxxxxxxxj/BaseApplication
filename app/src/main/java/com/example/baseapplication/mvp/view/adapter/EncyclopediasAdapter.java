package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.Encyclopedias;
import com.example.baseapplication.mvp.view.adapter.base.CommonAdapter;
import com.example.baseapplication.mvp.view.adapter.base.ViewHolder;
import com.example.baseapplication.mvp.view.widget.ScaleImageView;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.QMUIDisplayHelper;
import com.example.baseapplication.util.StringUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 19:03
 */
public class EncyclopediasAdapter<T> extends CommonAdapter<T> {

    public EncyclopediasAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_encyclopedias, position);
        LinearLayout ll_item_encyclopedias_root = viewHolder.getView(R.id.ll_item_encyclopedias_root);
        RelativeLayout rl_item_encyclopedias_picorvideo = viewHolder.getView(R.id.rl_item_encyclopedias_picorvideo);
        final ScaleImageView siv_item_encyclopedias = viewHolder.getView(R.id.siv_item_encyclopedias);
        TextView tv_item_encyclopedias_txt = viewHolder.getView(R.id.tv_item_encyclopedias_txt);
        TextView tv_item_encyclopedias_num = viewHolder.getView(R.id.tv_item_encyclopedias_num);
        ImageView iv_item_encyclopedias_userimg = viewHolder.getView(R.id.iv_item_encyclopedias_userimg);
        ImageView iv_item_encyclopedias_video = viewHolder.getView(R.id.iv_item_encyclopedias_video);
        TextView tv_item_encyclopedias_name = viewHolder.getView(R.id.tv_item_encyclopedias_name);
        final Encyclopedias item = (Encyclopedias) mDatas
                .get(position);
        if (item != null) {
            if (position == 0 || position == 1) {
                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) ll_item_encyclopedias_root.getLayoutParams();
                layoutParams.topMargin = QMUIDisplayHelper.dp2px(mContext, 10);
                ll_item_encyclopedias_root.setLayoutParams(layoutParams);
            } else {
                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) ll_item_encyclopedias_root.getLayoutParams();
                layoutParams.topMargin = QMUIDisplayHelper.dp2px(mContext, 5);
                ll_item_encyclopedias_root.setLayoutParams(layoutParams);
            }
            StringUtil.setText(tv_item_encyclopedias_txt, item.getTitle(), "", View.VISIBLE, View.GONE);
            StringUtil.setText(tv_item_encyclopedias_num, item.getRealReadNum() + "", "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_encyclopedias_name, item.getSource(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.displayImage(mContext, item.getSourceIcon(), iv_item_encyclopedias_userimg);
            if (StringUtil.isNotEmpty(item.getListCover())) {
                siv_item_encyclopedias.setDrawTopRid(true);
                rl_item_encyclopedias_picorvideo.setVisibility(View.VISIBLE);
                int imgWidth = (QMUIDisplayHelper.getScreenWidth(mContext) - QMUIDisplayHelper.dp2px(mContext, 25)) / 2;
                int imgHeight = 0;
                if (item.getListCoverWeight() > 0) {
                    imgHeight = imgWidth * item.getListCoverHeight() / item.getListCoverWeight();
                }
                siv_item_encyclopedias.setImageWidth(imgWidth);
                siv_item_encyclopedias.setImageHeight(imgHeight);
                GlideUtil.displayImage(mContext, item.getListCover(), siv_item_encyclopedias, imgWidth, imgHeight);
                if (item.isVideo()) {
                    iv_item_encyclopedias_video.setVisibility(View.VISIBLE);
                    iv_item_encyclopedias_video.bringToFront();
                } else {
                    iv_item_encyclopedias_video.setVisibility(View.GONE);
                }
            } else {
                rl_item_encyclopedias_picorvideo.setVisibility(View.GONE);
            }
        }
        return viewHolder.getConvertView();
    }
}
