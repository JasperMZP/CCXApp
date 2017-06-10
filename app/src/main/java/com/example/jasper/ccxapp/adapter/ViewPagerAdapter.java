package com.example.jasper.ccxapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

	ArrayList<View> childViewsOfViewPager;
	
	
	public ViewPagerAdapter(ArrayList<View> childViewOfViewPager) {
		
		super();
		this.childViewsOfViewPager = childViewOfViewPager;
	}

	@Override
	public int getCount() {
		
		if(childViewsOfViewPager != null){
			return childViewsOfViewPager.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}


	@Override
	public void destroyItem(View container, int position, Object object) {
		
		((ViewPager) container).removeView(childViewsOfViewPager.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(childViewsOfViewPager.get(position));
		return childViewsOfViewPager.get(position);
	}

}
