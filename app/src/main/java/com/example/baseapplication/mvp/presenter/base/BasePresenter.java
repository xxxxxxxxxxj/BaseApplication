package com.example.baseapplication.mvp.presenter.base;

import android.content.Context;

import com.example.baseapplication.mvp.view.iview.base.IBaseView;

/**
 * author:  ljy
 * date：   2018/3/18
 * description: Presenter基类
 * <a>https://www.jianshu.com/p/1f91cfd68d48</a>
 */
public abstract class BasePresenter<V extends IBaseView> {
    protected V mIView;
    protected Context mContext;

    public BasePresenter(V iView) {
        this.mIView = iView;
    }

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public BasePresenter(Context mContext, V iView) {
        this.mContext = mContext;
        this.mIView = iView;
    }

    public BasePresenter() {
    }

    /**
     * 释放引用，防止内存泄露
     */
    public void destroy() {
        this.mIView = null;
    }
}
