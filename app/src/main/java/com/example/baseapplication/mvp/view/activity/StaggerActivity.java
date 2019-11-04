package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.EncyclopediasTitleBean;
import com.example.baseapplication.mvp.presenter.StaggerActivityPresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.adapter.ViewPagerAdapter;
import com.example.baseapplication.mvp.view.fragment.EncyclopediasFragment;
import com.example.baseapplication.mvp.view.iview.IStaggerActivityView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 瀑布流界面
 */
public class StaggerActivity extends BaseActivity<StaggerActivityPresenter> implements IStaggerActivityView {
    @BindView(R.id.stl_encyclopedias)
    SlidingTabLayout stlEncyclopedias;
    @BindView(R.id.vp_encyclopedias)
    ViewPager vpEncyclopedias;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int currentTabIndex;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_stagger;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        initToolBar(toolbar, true, "宠物百科");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        stlEncyclopedias.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RingLog.e("TAG", "onTabSelect position = " + position);
                currentTabIndex = position;
                vpEncyclopedias.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                RingLog.e("TAG", "ponTabReselect position = " + position);
                if (currentTabIndex == position) {//刷新
                    autoRefresh();
                }
            }
        });
        vpEncyclopedias.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                stlEncyclopedias.setCurrentTab(currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void autoRefresh() {
        EncyclopediasFragment fragment = (EncyclopediasFragment) viewPagerAdapter.getItem(currentTabIndex);
        fragment.autoRefresh();
    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.getTab();
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected StaggerActivityPresenter createPresenter() {
        return new StaggerActivityPresenter(mActivity, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getTabSuccess(EncyclopediasTitleBean data) {
        hideLoadDialog();
        RingLog.e("getTabSuccess() data = " + data);
        if (data != null) {
            List<EncyclopediasTitleBean.EncyclopediasTitle> encyclopediaClassifications = data.getEncyclopediaClassifications();
            if (encyclopediaClassifications != null && encyclopediaClassifications.size() > 0) {
                for (int i = 0; i < encyclopediaClassifications.size(); i++) {
                    mFragments.add(new EncyclopediasFragment(encyclopediaClassifications.get(i).getId()));
                }
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments, encyclopediaClassifications);
                vpEncyclopedias.setAdapter(viewPagerAdapter);
                stlEncyclopedias.setViewPager(vpEncyclopedias);
                if (currentTabIndex < 0) {
                    currentTabIndex = 0;
                } else if (currentTabIndex >= encyclopediaClassifications.size()) {
                    currentTabIndex = encyclopediaClassifications.size() - 1;
                }
                stlEncyclopedias.setCurrentTab(currentTabIndex);
                vpEncyclopedias.setCurrentItem(currentTabIndex);
            }
        }
    }

    @Override
    public void getTabFail(int status, String desc) {
        hideLoadDialog();
        RingLog.e("getTabFail() status = " + status + "---desc = " + desc);
    }
}
