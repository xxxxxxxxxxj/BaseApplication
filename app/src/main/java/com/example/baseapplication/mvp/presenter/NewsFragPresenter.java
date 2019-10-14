package com.example.baseapplication.mvp.presenter;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.INewsFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 20:09
 */
public class NewsFragPresenter  extends BasePresenter<INewsFragView> {
    public NewsFragPresenter(INewsFragView iNewsFragView) {
        super(iNewsFragView);
    }
}
