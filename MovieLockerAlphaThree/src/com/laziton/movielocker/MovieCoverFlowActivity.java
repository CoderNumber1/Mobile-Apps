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

        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
        dataService.Open();
        final ArrayList<Movie> movies = dataService.GetMovies();
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
            public Fragment getItem(int pos) {
                int movieId = movies.get(pos).getId();
                return CoverDisplayFragment.newInstance(movieId);
            }
        }); 

        pager.setOffscreenPageLimit(9);
        pager.setCurrentItem(0);
//        UUID crimeId = (UUID)getIntent().getSerializableExtra(ContactFragment.CONTACT_ID);
//        for (int i = 0; i < contacts.size(); i++) {
//            if (contacts.get(i).getId().equals(crimeId)) {
//                pager.setCurrentItem(i);
//                break;
//            } 
//        }
    }
//	@Override
//	protected Fragment createFragment() {
//		// TODO Auto-generated method stub
//		return new MovieCoverFlowFragment();
//	}
	
////	public static class MovieCoverList extends ViewPager {
////
////		public MovieCoverList(Context context) {
////			super(context);
////		}
////		
////	}
//
//	public static class MovieCoverFlowFragment extends Fragment{
//		TextView txtMovieName;
//		CoverFlow cvrMovies;
//		
//		@Override
//		public void onCreate(Bundle savedInstanceState) {
//			// TODO Auto-generated method stub
//			super.onCreate(savedInstanceState);
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			View result = inflater.inflate(R.layout.movie_coverflow_fragment, container, false);
//			
//			this.txtMovieName = (TextView)result.findViewById(R.id.txtMovieName);
//			this.cvrMovies = (CoverFlow)result.findViewById(R.id.cvrMovies);
//			
//			this.setupCoverFlow(this.cvrMovies, true);
//			
//			return result;
//		}
//		
//		/**
//	     * Setup cover flow.
//	     * 
//	     * @param mCoverFlow
//	     *            the m cover flow
//	     * @param reflect
//	     *            the reflect
//	     */
//	    private void setupCoverFlow(final CoverFlow mCoverFlow, final boolean reflect) {
//	        BaseAdapter coverImageAdapter;
//	        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
//			dataService.Open();
//			
//	        if (reflect) {
//	            coverImageAdapter = new ReflectingImageAdapter(new ResourceImageAdapter(this.getActivity(), dataService.GetMovies()));
//	        } else {
//	            coverImageAdapter = new ResourceImageAdapter(this.getActivity(), dataService.GetMovies());
//	        }
//	        dataService.Close();
//	        mCoverFlow.setAdapter(coverImageAdapter);
////	        mCoverFlow.setSelection(0, true);
//	        setupListeners(mCoverFlow);
//	    }
//
//	    /**
//	     * Sets the up listeners.
//	     * 
//	     * @param mCoverFlow
//	     *            the new up listeners
//	     */
//	    private void setupListeners(final CoverFlow mCoverFlow) {
//	        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
//	            @Override
//	            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {
////	                textView.setText("Item clicked! : " + id);
//	            }
//
//	        });
//	        mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
//	            @Override
//	            public void onItemSelected(final AdapterView< ? > parent, final View view, final int position, final long id) {
////	                textView.setText("Item selected! : " + id);
//	            }
//
//	            @Override
//	            public void onNothingSelected(final AdapterView< ? > parent) {
////	                textView.setText("Nothing clicked!");
//	            }
//	        });
//	    }	    
//	}
}