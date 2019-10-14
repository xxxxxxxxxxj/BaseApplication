package com.example.baseapplication.mvp.presenter;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IMainActivityView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 10:15
 */
public class MainActivityPresenter extends BasePresenter<IMainActivityView> {
    public MainActivityPresenter(IMainActivityView iMainActivityView) {
        super(iMainActivityView);
    }

    /**
     * 获取最新版本
     */
    public void checkversion() {

    }
}
