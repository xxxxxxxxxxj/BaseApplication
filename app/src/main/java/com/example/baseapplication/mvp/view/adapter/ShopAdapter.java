package com.example.baseapplication.mvp.view.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.baseapplication.R;
import com.example.baseapplication.util.StringUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-25 10:39
 */
public class ShopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Activity mActivity;
    public OnItemAddListener onItemAddListener = null;

    public interface OnItemAddListener {
        public void OnItemAdd(int position, ImageView imageView);
    }

    public void setOnItemAddListener(
            OnItemAddListener onItemAddListener) {
        this.onItemAddListener = onItemAddListener;
    }

    public ShopAdapter(Activity mActivity, int layoutResId, List<String> data) {
        super(layoutResId, data);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_item_shopfrag = helper.getView(R.id.tv_item_shopfrag);
        ImageView iv_item_appointment_item_add = helper.getView(R.id.iv_item_appointment_item_add);
        StringUtil.setText(tv_item_shopfrag, item, "", View.VISIBLE, View.VISIBLE);
        if (helper.getLayoutPosition() == 19) {
            iv_item_appointment_item_add.setVisibility(View.VISIBLE);
        } else {
            iv_item_appointment_item_add.setVisibility(View.GONE);
        }
        tv_item_shopfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemAddListener != null) {
                    onItemAddListener.OnItemAdd(helper.getLayoutPosition(), iv_item_appointment_item_add);
                }
            }
        });
    }
}
