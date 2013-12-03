package com.laziton.movielocker;

import android.support.v4.app.Fragment;

public class GenreListActivity extends SingleFragmentHost {

	@Override
	protected Fragment createFragment() {
		return new GenreListFragment();
	}

}
