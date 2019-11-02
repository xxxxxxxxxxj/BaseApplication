package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;

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
    private int currentTabIndex;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_stagger;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

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
                    EncyclopediasFragment encyclopediasFragment = new EncyclopediasFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", encyclopediaClassifications.get(i).getId());
                    encyclopediasFragment.setArguments(bundle);
                    mFragments.add(encyclopediasFragment);
                }
                vpEncyclopedias.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, encyclopediaClassifications));
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
