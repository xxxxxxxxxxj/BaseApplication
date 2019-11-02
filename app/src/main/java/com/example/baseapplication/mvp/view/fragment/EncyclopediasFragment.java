package com.example.baseapplication.mvp.view.fragment;

import androidx.annotation.NonNull;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.Encyclopedias;
import com.example.baseapplication.mvp.presenter.EncyclopediasFragPresenter;
import com.example.baseapplication.mvp.view.adapter.EncyclopediasAdapter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.IEncyclopediasFragView;
import com.huewu.pla.lib.MultiColumnListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:47
 */
public class EncyclopediasFragment extends BaseFragment<EncyclopediasFragPresenter> implements IEncyclopediasFragView {
    @BindView(R.id.mclv_encyclopedias_fragment)
    MultiColumnListView mclvEncyclopediasFragment;
    @BindView(R.id.srl_newsfrag)
    SmartRefreshLayout srlNewsfrag;
    private Map<String, String> postParams = new LinkedHashMap<String, String>();
    private int page = 1;
    private List<Encyclopedias> list = new ArrayList<Encyclopedias>();
    private int pageSize = 10;
    private EncyclopediasAdapter<Encyclopedias> encyclopediasAdapter;

    @Override
    protected EncyclopediasFragPresenter createPresenter() {
        return new EncyclopediasFragPresenter(getContext(), this);
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
        return R.layout.fragment_stagger;
    }

    @Override
    protected void initView() {
        list.clear();
        encyclopediasAdapter = new EncyclopediasAdapter<Encyclopedias>(mActivity, list);
        mclvEncyclopediasFragment.setAdapter(encyclopediasAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        srlNewsfrag.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srlNewsfrag.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                loadMore();
            }
        });
    }

    private void refresh() {
        page = 1;
        list.clear();
        setRequest();
    }

    private void loadMore() {
        setRequest();
    }

    private void setRequest() {
        postParams.clear();
        postParams.put("classificationId", String.valueOf(getArguments().getInt("id", 0)));
        postParams.put("page", String.valueOf(page));
        mPresenter.getEncyclopedias(postParams);
    }

    @Override
    public void getEncyclopediasSuccess(List<Encyclopedias> data) {
        RingLog.e("getEncyclopediasSuccess() data = " + data);
        hideLoadDialog();
        if (data != null && data.size() > 0) {
            list.addAll(data);
            if (page == 1) {
                pageSize = data.size();
                srlNewsfrag.finishRefresh();
                srlNewsfrag.resetNoMoreData();
            } else {
                if (data.size() < pageSize) {
                    srlNewsfrag.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                } else {
                    srlNewsfrag.finishLoadMore();
                }
            }
            page++;
        } else {
            if (page == 1) {
                srlNewsfrag.finishRefresh();
                srlNewsfrag.resetNoMoreData();
            } else {
                srlNewsfrag.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            }
        }
        encyclopediasAdapter.notifyDataSetChanged();
    }

    @Override
    public void getEncyclopediasFail(int code, String msg) {
        hideLoadDialog();
        RingLog.e("getEncyclopediasFail() code = " + code + "---msg = " + msg);
        if (page == 1) {
            srlNewsfrag.finishRefresh();
            srlNewsfrag.resetNoMoreData();
        } else {
            srlNewsfrag.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        encyclopediasAdapter.notifyDataSetChanged();
    }

    public void autoRefresh() {
        mclvEncyclopediasFragment.post(new Runnable() {
            @Override
            public void run() {
                srlNewsfrag.autoRefresh();
            }
        });
    }

    @Override
    protected void loadData() {
        showLoadDialog();
        autoRefresh();
    }
}
