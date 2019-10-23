package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.PostBean;
import com.example.baseapplication.mvp.view.widget.NoScollFullGridLayoutManager;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.StringUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-23 11:28
 */
public class PostAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {
    private Activity mActivity;

    public PostAdapter(Activity mActivity, int layoutResId, List<PostBean> data) {
        super(layoutResId, data);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        TextView tv_item_newsfrag_title = helper.getView(R.id.tv_item_newsfrag_title);
        RecyclerView rv_item_newsfrag_img = helper.getView(R.id.rv_item_newsfrag_img);
        TextView tv_item_newsfrag_time = helper.getView(R.id.tv_item_newsfrag_time);
        TextView tv_item_newsfrag_num = helper.getView(R.id.tv_item_newsfrag_num);
        ImageView iv_item_newsfrag_userimg = helper.getView(R.id.iv_item_newsfrag_userimg);
        TextView tv_item_newsfrag_name = helper.getView(R.id.tv_item_newsfrag_name);
        if (item != null) {
            GlideUtil.displayImageNoDefault(mContext, item.getHeadImg(), iv_item_newsfrag_userimg);
            if (item.getMedia() != null && item.getMedia().size() > 0) {
                rv_item_newsfrag_img.setVisibility(View.VISIBLE);
                rv_item_newsfrag_img.setHasFixedSize(true);
                rv_item_newsfrag_img.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new NoScollFullGridLayoutManager(rv_item_newsfrag_img, mContext, 3, GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_newsfrag_img.setLayoutManager(noScollFullGridLayoutManager);
                ImgAdapter imgAdapter = new ImgAdapter(mActivity,R.layout.item_img, item.getMedia(), 197, 137);
                rv_item_newsfrag_img.setAdapter(imgAdapter);
            } else {
                rv_item_newsfrag_img.setVisibility(View.GONE);
            }
            if (StringUtil.isNotEmpty(item.getTitle())) {
                StringUtil.setText(tv_item_newsfrag_title, item.getTitle(), "", View.VISIBLE, View.VISIBLE);
            } else if (StringUtil.isNotEmpty(item.getContent())) {
                StringUtil.setText(tv_item_newsfrag_title, item.getContent(), "", View.VISIBLE, View.VISIBLE);
            }
            StringUtil.setText(tv_item_newsfrag_time, item.getCreateTime(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_newsfrag_num, item.getComments() + "评论", "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_newsfrag_name, item.getUserName(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}
