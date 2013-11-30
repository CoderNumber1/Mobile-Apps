package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionMembersActivity.CollectionMembersFragment;
import com.laziton.movielocker.GenreListFragment.GenresAdapter;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;

public class MovieListActivity extends SingleFragmentHost {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new MovieListFragment();
	}
	
	public static class MovieListFragment extends ListFragment {
		public static int EDIT_FILTER_CODE = 1;
		
		private ArrayList<Movie> movies;
		private MoviesAdapter moviesAdapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			IFilteredMovieDataService dataService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
			dataService.Open();
			this.movies = dataService.GetFilteredMovies();
			dataService.Close();
			
			this.moviesAdapter = new MoviesAdapter(this.movies);
			this.setListAdapter(moviesAdapter);
			this.setRetainInstance(true);
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = super.onCreateView(inflater, container, savedInstanceState);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
			
			ListView list = (ListView)v.findViewById(android.R.id.list);
	        
	        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
	        	this.registerForContextMenu(list);
	        }
	        else{
	        	list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	        	list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						switch(item.getItemId()){
							case R.id.menu_delete:
								MoviesAdapter adapter = (MoviesAdapter)getListAdapter();
								IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
								dataService.Open();
								
								for(int i = adapter.getCount() - 1; i >= 0; i--){
									if(getListView().isItemChecked(i)){
										dataService.DeleteMovie(adapter.getItem(i));
									}
								}
								dataService.Close();
								mode.finish();
								adapter.notifyDataSetChanged();
								return true;
							default:
								return false;
						}
					}

					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.movie_list_item_context, menu);
						return true;
					}

					@Override
					public void onDestroyActionMode(ActionMode mode) { }

					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
					}

					@Override
					public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2, boolean arg3) { }
	        	});
	        }
			
			return v;
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.main, menu);
	        menu.findItem(R.id.menu_done).setVisible(false);
	        menu.findItem(R.id.menu_save).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.menu_add:
					Intent genreAdd = new Intent(getActivity(), MovieActivity.class);
					startActivityForResult(genreAdd, 0);
					break;
				case R.id.menu_filter:
					Intent filterEdit = new Intent(MovieListFragment.this.getActivity(), MovieFilterActivity.class);
					MovieListFragment.this.startActivityForResult(filterEdit, MovieListFragment.EDIT_FILTER_CODE);
					break;
				case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
			}
			
			return true;
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(requestCode == EDIT_FILTER_CODE){
				IFilteredMovieDataService dataService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
				this.moviesAdapter.clear();
				this.moviesAdapter.addAll(dataService.GetFilteredMovies());
				this.moviesAdapter.notifyDataSetChanged();
			}
		}

		public void onListItemClick(ListView listView, View view, int position, long id) {
	        Movie movie = ((MoviesAdapter)getListAdapter()).getItem(position);
	        Intent genreEdit = new Intent(getActivity(), MovieActivity.class);
	        genreEdit.putExtra(MovieActivity.MOVIE_ID, movie.getId());
	        startActivityForResult(genreEdit, 0);
	    }

		public class MoviesAdapter extends ArrayAdapter<Movie>{
			public MoviesAdapter(ArrayList<Movie> movies){
				super(getActivity(), android.R.layout.simple_list_item_1, movies);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
	            if (null == convertView) {
	                convertView = getActivity().getLayoutInflater()
	                    .inflate(android.R.layout.simple_list_item_1, null);
	            }

	            Movie movie = getItem(position);
	            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
	            titleTextView.setText(movie.getName());

	            return convertView;
			}
		}
	}
}
