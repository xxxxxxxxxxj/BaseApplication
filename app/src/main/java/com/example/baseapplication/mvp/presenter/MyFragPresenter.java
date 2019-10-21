package com.example.baseapplication.mvp.presenter;

import android.content.Context;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IMyFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:13
 */
public class MyFragPresenter extends BasePresenter<IMyFragView> {
    public MyFragPresenter(Context mContext, IMyFragView iMyFragView) {
        super(mContext, iMyFragView);
    }
}