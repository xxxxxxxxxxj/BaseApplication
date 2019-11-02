package com.example.baseapplication.mvp.view.iview;

import com.example.baseapplication.mvp.model.entity.EncyclopediasTitleBean;
import com.example.baseapplication.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:33
 */
public interface IStaggerActivityView extends IBaseView {
    void getTabSuccess(EncyclopediasTitleBean response);

    void getTabFail(int status, String desc);
}
