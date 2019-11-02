package com.example.baseapplication.mvp.view.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.example.baseapplication.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-19 16:04
 */
public class GiftCardComponent implements Component {
    private final View.OnClickListener clickListener;
    private final Activity context;

    public GiftCardComponent(Activity context, View.OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_giftcard, null);
        ImageView iv_guide_eattime2 = ll.findViewById(R.id.iv_guide_eattime2);
        iv_guide_eattime2.setOnClickListener(clickListener);
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return -35;
    }

    @Override
    public int getYOffset() {
        return 200;
    }
}
