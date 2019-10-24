package com.example.baseapplication.mvp.view.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.presenter.MainFragPresenter;
import com.example.baseapplication.mvp.view.adapter.MyPagerAdapter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.IMainFragView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:04
 */
public class MainFragment extends BaseFragment<MainFragPresenter> implements IMainFragView {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.stl_main)
    SlidingTabLayout stlMain;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源",
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private MyPagerAdapter mAdapter;
    private int currentTabIndex;

    @Override
    protected MainFragPresenter createPresenter() {
        return new MainFragPresenter(getContext(), this);
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
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < mTitles.length; i++) {
            mFragments.add(new NewsFragment(i));
        }
        mAdapter = new MyPagerAdapter(mActivity.getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);
        stlMain.setViewPager(viewPager);
        stlMain.setCurrentTab(currentTabIndex);
        viewPager.setCurrentItem(currentTabIndex);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        stlMain.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RingLog.e("TAG", "onTabSelect position = " + position);
                currentTabIndex = position;
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                RingLog.e("TAG", "ponTabReselect position = " + position);
                if (currentTabIndex == position) {//刷新
                    NewsFragment fragment = (NewsFragment) mAdapter.getItem(position);
                    fragment.autoRefresh();
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                stlMain.setCurrentTab(currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void loadData() {

    }
}
