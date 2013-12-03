package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.images.ImageManager;
import com.laziton.movielocker.store.FilteredMovieStore;
import com.laziton.movielocker.store.FilteredMovieStore.IMovieStoreMonitor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class MovieCoverFlowViewPager extends ViewPager implements IMovieStoreMonitor {

	public MovieCoverFlowViewPager(Context context) {
		super(context);
	}

	@Override
	protected void onAttachedToWindow() {
		FilteredMovieStore.getInstance().registerAdapter(this);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		FilteredMovieStore.getInstance().unRegisterAdapter(this);
		super.onDetachedFromWindow();
	}

	@Override
	public void onStoreUpdated() {
		this.getAdapter().notifyDataSetChanged();
	}
	
	public static class CoverFlowAdapter extends FragmentStatePagerAdapter{
		private ArrayList<Movie> movies;
		
		public CoverFlowAdapter(FragmentManager fm) {
			super(fm);
			this.movies = FilteredMovieStore.getInstance().getMovies();
		}
		
		@Override
		public float getPageWidth(int position) {
			return (0.5f);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
        public int getCount() {
            return movies.size();
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageManager.getInstance().cleanImageView(((CoverDisplayFragment)object).imgMovieCover);
			super.destroyItem(container, position, object);
		}

		@Override
        public Fragment getItem(int pos) {
            int movieId = movies.get(pos).getId();
            return CoverDisplayFragment.newInstance(movieId);
        }
	}

}
