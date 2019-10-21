package com.example.baseapplication.mvp.view.fragment;

import android.widget.TextView;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.NewsFragPresenter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.INewsFragView;

import butterknife.BindView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 20:08
 */
public class NewsFragment extends BaseFragment<NewsFragPresenter> implements INewsFragView {
    @BindView(R.id.tv_newsfrag)
    TextView tvNewsfrag;
    private int index;

    @Override
    protected NewsFragPresenter createPresenter() {
        return new NewsFragPresenter(getContext(),this);
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
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        tvNewsfrag.setText(String.valueOf(index));
    }

    @Override
    protected void initData() {
        index = getArguments().getInt("index", 0);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }
}
