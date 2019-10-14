package com.example.baseapplication.mvp.view.fragment;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.ShopFragPresenter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.IShopFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:09
 */
public class ShopFragment extends BaseFragment<ShopFragPresenter> implements IShopFragView {
    @Override
    protected ShopFragPresenter createPresenter() {
        return new ShopFragPresenter(this);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }
}
