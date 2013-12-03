package com.laziton.movielocker;

import android.support.v4.app.Fragment;

public class MainActivity extends DualFragmentHost {
	public static int ACTIVITY_CREATE_MOVIE = 3;
	public static String SELECTED_MOVIE_ID = "selectedMovieId";
	public static String SELECTED_MOVIE_POSITION = "selectedMoviePosition";

	@Override
	protected Fragment createLeftPane() {
		return new MovieListFragment();
	}

	@Override
	protected Fragment createRightPane() {
		return new MovieCoverFlowFragment();
	}

}
