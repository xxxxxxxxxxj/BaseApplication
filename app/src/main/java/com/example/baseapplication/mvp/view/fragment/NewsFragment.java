package com.example.baseapplication.mvp.view.fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.BannerBean;
import com.example.baseapplication.mvp.model.entity.PostBean;
import com.example.baseapplication.mvp.presenter.NewsFragPresenter;
import com.example.baseapplication.mvp.view.adapter.PostAdapter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.INewsFragView;
import com.example.baseapplication.util.GlideImageLoader;
import com.example.baseapplication.util.JumpToUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

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
 * @date zhoujunxia on 2019-10-14 20:08
 */
public class NewsFragment extends BaseFragment<NewsFragPresenter> implements OnBannerListener, INewsFragView {
    @BindView(R.id.rv_newsfrag)
    RecyclerView rvNewsfrag;
    @BindView(R.id.srl_newsfrag)
    SmartRefreshLayout srlNewsfrag;
    private int index;
    private Map<String, String> bannerParams = new LinkedHashMap<String, String>();
    private Map<String, String> postParams = new LinkedHashMap<String, String>();
    private int page = 1;
    private List<BannerBean> bannerList = new ArrayList<BannerBean>();
    private List<PostBean> postList = new ArrayList<PostBean>();
    private Banner banner_newsfrag;
    private ArrayList<String> imgList = new ArrayList<String>();
    private PostAdapter postAdapter;
    private int pageSize = 10;

    public NewsFragment(int index) {
        super();
        this.index = index;
    }

    @Override
    protected NewsFragPresenter createPresenter() {
        return new NewsFragPresenter(getContext(), this);
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
        rvNewsfrag.setHasFixedSize(true);
        rvNewsfrag.setLayoutManager(new LinearLayoutManager(mActivity));
        postAdapter = new PostAdapter(mActivity, R.layout.item_newsfrag, postList);
        View top = getLayoutInflater().inflate(R.layout.newsfrag_top_banner, (ViewGroup) rvNewsfrag.getParent(), false);
        banner_newsfrag = (Banner) top.findViewById(R.id.banner_newsfrag);
        postAdapter.addHeaderView(top);
        rvNewsfrag.setAdapter(postAdapter);
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

    @Override
    protected void loadData() {
        showLoadDialog();
        bannerParams.clear();
        bannerParams.put("category", "3");
        mPresenter.getBanner(bannerParams);
        refresh();
    }

    private void refresh() {
        page = 1;
        postList.clear();
        setRequest();
    }

    private void loadMore() {
        setRequest();
    }

    private void setRequest() {
        postParams.clear();
        postParams.put("page", String.valueOf(page));
        mPresenter.getPost(index, postParams);
    }

    @Override
    public void getPostSuccess(List<PostBean> data) {
        RingLog.e("getPostSuccess() data = " + data);
        hideLoadDialog();
        if (data != null && data.size() > 0) {
            postList.addAll(data);
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
                postAdapter.setEmptyView(setEmptyViewBase(2, "暂无数据~", null));
            } else {
                srlNewsfrag.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            }
        }
    }

    @Override
    public void getPostFail(int code, String msg) {
        hideLoadDialog();
        RingLog.e("getPostFail() code = " + code + "---msg = " + msg);
        if (page == 1) {
            srlNewsfrag.finishRefresh();
            srlNewsfrag.resetNoMoreData();
            postAdapter.setEmptyView(setEmptyViewBase(1, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh();
                }
            }));
        } else {
            srlNewsfrag.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
    }

    @Override
    public void getBannerSuccess(List<BannerBean> data) {
        RingLog.e("getBannerSuccess() data = " + data);
        hideLoadDialog();
        if (data != null && data.size() > 0) {
            banner_newsfrag.setVisibility(View.VISIBLE);
            bannerList.clear();
            bannerList.addAll(data);
            setBanner();
        } else {
            banner_newsfrag.setVisibility(View.GONE);
        }
    }

    @Override
    public void getBannerFail(int code, String msg) {
        hideLoadDialog();
        RingLog.e("getBannerFail() code = " + code + "---msg = " + msg);
    }

    private void setBanner() {
        imgList.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            imgList.add(bannerList.get(i).getImg());
        }
        banner_newsfrag.setImages(imgList)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        RingLog.e("position:" + position);
        if (bannerList != null && bannerList.size() > 0 && bannerList.size() > position) {
            BannerBean bannerBean = bannerList.get(position);
            if (bannerBean != null) {
                JumpToUtil.jumpTo(bannerBean.getPoint(), bannerBean.getBackup());
            }
        }
    }
}