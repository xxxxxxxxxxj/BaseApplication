package com.example.baseapplication.mvp.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/5/11 22:49
 */
public class NoScollGridView extends GridView {
    public NoScollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScollGridView(Context context) {
        super(context);
    }

    public NoScollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
