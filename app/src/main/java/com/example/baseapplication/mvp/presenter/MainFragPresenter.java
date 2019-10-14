package com.example.baseapplication.mvp.presenter;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IMainFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:07
 */
public class MainFragPresenter extends BasePresenter<IMainFragView> {
    public MainFragPresenter(IMainFragView iMainFragView) {
        super(iMainFragView);
    }
}
