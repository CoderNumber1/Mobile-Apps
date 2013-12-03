package com.laziton.movielocker;

import android.support.v4.app.Fragment;

public class MovieListActivity extends SingleFragmentHost {
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new MovieListFragment();
	}
}
