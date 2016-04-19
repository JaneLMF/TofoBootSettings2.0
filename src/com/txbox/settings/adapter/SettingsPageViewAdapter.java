package com.txbox.settings.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class SettingsPageViewAdapter extends PagerAdapter {
	public List<View> mListViews;

	public SettingsPageViewAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mListViews.get(arg1));
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		try {
			if (mListViews.get(arg1).getParent() == null)
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			else {
				((ViewGroup) mListViews.get(arg1).getParent()).removeView(mListViews.get(arg1));
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			}
		} catch (Exception e) {
			Log.d("parent=", "" + mListViews.get(arg1).getParent());
			e.printStackTrace();
		}
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
}
