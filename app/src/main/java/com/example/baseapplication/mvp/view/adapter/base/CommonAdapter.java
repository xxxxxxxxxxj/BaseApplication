package com.example.baseapplication.mvp.view.adapter.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.example.baseapplication.util.SharedPreferenceUtil;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Activity mContext;
	protected List<T> mDatas;
	protected SharedPreferenceUtil spUtil;
	protected int flag = -1;

	public CommonAdapter(Activity mContext, List<T> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;
		mInflater = LayoutInflater.from(mContext);
		spUtil = SharedPreferenceUtil.getInstance(mContext);
	}

	public CommonAdapter(Activity mContext, List<T> mDatas, int flag) {
		this.mContext = mContext;
		this.mDatas = mDatas;
		this.flag = flag;
		mInflater = LayoutInflater.from(mContext);
		spUtil = SharedPreferenceUtil.getInstance(mContext);
	}

	public void clearDeviceList() {
		if (mDatas != null) {
			mDatas.clear();
		}
		notifyDataSetChanged();
	}

	public void setData(List<T> mDatas) {
		if (mDatas != null) {
			this.mDatas = mDatas;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
