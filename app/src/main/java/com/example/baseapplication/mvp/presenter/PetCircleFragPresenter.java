package com.example.baseapplication.mvp.presenter;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IPetCircleFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:13
 */
public class PetCircleFragPresenter extends BasePresenter<IPetCircleFragView> {
    public PetCircleFragPresenter(IPetCircleFragView iPetCircleFragView) {
        super(iPetCircleFragView);
    }
}