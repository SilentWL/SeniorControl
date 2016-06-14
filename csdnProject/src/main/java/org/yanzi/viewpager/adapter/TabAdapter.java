package org.yanzi.viewpager.adapter;

import org.yanzi.constant.Constant;
import org.yanzi.fragment.MainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

	public TabAdapter(FragmentManager fm) {
		
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		MainFragment fragment = new MainFragment();
		Bundle b = new Bundle();
		String title = Constant.TITLES[position];
		b.putString("TITLES", title);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return Constant.TITLES[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constant.TITLES.length;
	}

}
