/**
 * 
 */
package org.giwi.android.blackout.model;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * @author b3605
 * 
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
	private final List<Fragment> fragments;

	public List<Fragment> getFragments() {
		return fragments;
	}

	/**
	 * @param fm
	 * @param fragments
	 */
	public MyPagerAdapter(final FragmentManager fm, final List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(final int position) {
		return fragments.get(position);
	}

	@Override
	public void destroyItem(final ViewGroup container, final int position, final Object object) {
		if (position >= getCount()) {
			final FragmentManager manager = ((Fragment) object).getFragmentManager();
			final FragmentTransaction trans = manager.beginTransaction();
			trans.remove((Fragment) object);
			trans.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return fragments.size();
	}
}
