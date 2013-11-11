package com.laziton.movielocker;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.coverflow.CoverFlow;
import com.laziton.movielocker.coverflow.ReflectingImageAdapter;
import com.laziton.movielocker.coverflow.ResourceImageAdapter;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MovieCoverFlowActivity extends SingleFragmentHost {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new MovieCoverFlowFragment();
	}

	public static class MovieCoverFlowFragment extends Fragment{
		TextView txtMovieName;
		CoverFlow cvrMovies;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View result = inflater.inflate(R.layout.movie_coverflow_fragment, container, false);
			
			this.txtMovieName = (TextView)result.findViewById(R.id.txtMovieName);
			this.cvrMovies = (CoverFlow)result.findViewById(R.id.cvrMovies);
			
			this.setupCoverFlow(this.cvrMovies, true);
			
			return result;
		}
		
		/**
	     * Setup cover flow.
	     * 
	     * @param mCoverFlow
	     *            the m cover flow
	     * @param reflect
	     *            the reflect
	     */
	    private void setupCoverFlow(final CoverFlow mCoverFlow, final boolean reflect) {
	        BaseAdapter coverImageAdapter;
	        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			
	        if (reflect) {
	            coverImageAdapter = new ReflectingImageAdapter(new ResourceImageAdapter(this.getActivity(), dataService.GetMovies()));
	        } else {
	            coverImageAdapter = new ResourceImageAdapter(this.getActivity(), dataService.GetMovies());
	        }
	        dataService.Close();
	        mCoverFlow.setAdapter(coverImageAdapter);
	        mCoverFlow.setSelection(2, true);
	        setupListeners(mCoverFlow);
	    }

	    /**
	     * Sets the up listeners.
	     * 
	     * @param mCoverFlow
	     *            the new up listeners
	     */
	    private void setupListeners(final CoverFlow mCoverFlow) {
	        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {
//	                textView.setText("Item clicked! : " + id);
	            }

	        });
	        mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(final AdapterView< ? > parent, final View view, final int position, final long id) {
//	                textView.setText("Item selected! : " + id);
	            }

	            @Override
	            public void onNothingSelected(final AdapterView< ? > parent) {
//	                textView.setText("Nothing clicked!");
	            }
	        });
	    }	    
	}
}
