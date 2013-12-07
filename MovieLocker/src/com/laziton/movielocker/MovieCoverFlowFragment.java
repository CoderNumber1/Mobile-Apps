package com.laziton.movielocker;

import com.laziton.movielocker.store.FilteredMovieStore;
import com.laziton.movielocker.store.FilteredMovieStore.IMovieStoreMonitor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieCoverFlowFragment extends Fragment implements IMovieStoreMonitor, IDualPaneClient {
	ViewPager pager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = new MovieCoverFlowViewPager(getActivity());
        pager.setId(R.id.viewPager);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        pager.setAdapter(new MovieCoverFlowViewPager.CoverFlowAdapter(manager));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return this.pager;
	}

	@Override
	public void onAttach(Activity activity) {
		FilteredMovieStore.getInstance().registerAdapter(this);
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		FilteredMovieStore.getInstance().unRegisterAdapter(this);
		super.onDetach();
	}

	@Override
	public void onStoreUpdated() {
		this.pager.getAdapter().notifyDataSetChanged();
		this.pager.invalidate();
	}

	@Override
	public void recieveMessage(Bundle args) {
		if(args.containsKey(MainActivity.SELECTED_MOVIE_POSITION)){
			int movieId = args.getInt(MainActivity.SELECTED_MOVIE_POSITION);
			int position = movieId;
//			CoverFlowAdapter adapter = (CoverFlowAdapter)this.pager.getAdapter();
//			for(int i = adapter.getCount(); i >= 0; i--){
//				CoverDisplayFragment item = (CoverDisplayFragment)adapter.getItem(i);
//				if(item.movie.getId() == movieId){
//					position = i;
//					break;
//				}
//			}
			this.pager.setCurrentItem(position);
		}
	}
}
