package com.example.baseapplication.mvp.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.baseapplication.mvp.model.entity.EncyclopediasTitleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:49
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<EncyclopediasTitleBean.EncyclopediasTitle> list;
    private ArrayList<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, List<EncyclopediasTitleBean.EncyclopediasTitle> list) {
        super(fm);
        this.mFragments = mFragments;
        this.list = list;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
