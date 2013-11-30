package com.laziton.movielocker;

import java.util.ArrayList;
import java.util.UUID;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.coverflow.CoverFlow;
import com.laziton.movielocker.coverflow.ReflectingImageAdapter;
import com.laziton.movielocker.coverflow.ResourceImageAdapter;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;
import com.laziton.movielocker.images.ImageManager;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MovieCoverFlowActivity extends FragmentActivity {

	ViewPager pager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = new ViewPager(this);
        pager.setId(R.id.viewPager);
        setContentView(pager);

        IFilteredMovieDataService dataService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
        dataService.Open();
        final ArrayList<Movie> movies = dataService.GetFilteredMovies();
        dataService.Close();

        FragmentManager manager = getSupportFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
			public float getPageWidth(int position) {
				return (0.5f);
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
        });
	}
}
