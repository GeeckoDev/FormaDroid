package org.geeckodev.formadroid.adapters;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DaysPagerAdapter extends FragmentPagerAdapter implements Iterable<Fragment> {

	private final List<Fragment> fragments;

	public DaysPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new Vector<Fragment>();
	}
	
	public void clear() {
		this.fragments.clear();
	}
	
	public void addItem(Fragment frag) {
		this.fragments.add(frag);
	}
	
	public void setItem(int pos, Fragment frag) {
		this.fragments.set(pos, frag);
	}
	
	@Override
	public Iterator<Fragment> iterator() {
		return this.fragments.iterator();
	}
	
	@Override
	public Fragment getItem(int pos) {		
		return this.fragments.get(pos);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
