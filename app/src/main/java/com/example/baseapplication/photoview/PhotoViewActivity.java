package com.example.baseapplication.photoview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.model.entity.ImageInfo;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.adapter.PhotoViewPagerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 查看大图类
 */
public class PhotoViewActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    HackyViewPager viewPager;
    @BindView(R.id.points)
    LinearLayout points;
    private int prePosition;
    private PhotoViewPagerAdapter myViewPagerAdapter;
    private List<ImageInfo> imgList;
    public static final String KEY_PHOTOVIEW_IMGLIST = "imgList";
    public static final String KEY_PHOTOVIEW_POSITION = "position";

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        prePosition = savedInstanceState.getInt(KEY_PHOTOVIEW_POSITION);
        imgList = (List<ImageInfo>) savedInstanceState.getSerializable(KEY_PHOTOVIEW_IMGLIST);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        myViewPagerAdapter = new PhotoViewPagerAdapter(this, imgList);
        viewPager.setAdapter(myViewPagerAdapter);
        for (int i = 0; i < imgList.size(); i++) {
            //白点
            //根据viewPager的数量，添加白点指示器
            ImageView view = new ImageView(this);
            view.setBackgroundResource(R.drawable.point_back);
            //给点设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            //给控件设置边距
            params.leftMargin = 10;
            //给view设置参数
            view.setLayoutParams(params);
            //将图片添加到线性布局中
            points.addView(view);
        }
        points.getChildAt(prePosition).setBackgroundResource(R.drawable.point_white);
        viewPager.setCurrentItem(prePosition);
    }

    @Override
    protected void initEvent() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                position = position % imgList.size();
                //把前一个白变为黑
                points.getChildAt(prePosition).setBackgroundResource(R.drawable.point_back);
                //把当前白点变为黑点
                points.getChildAt(position).setBackgroundResource(R.drawable.point_white);
                //记录下当前位置(当前位置变白后，赋值给前一个点)
                prePosition = position;
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAllowFullScreen(true);
        setScreenRoate(true);
        setSteepStatusBar(true);
        super.onCreate(savedInstanceState);
        setSwipeBack(false);
    }
}
