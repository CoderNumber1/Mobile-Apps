package com.laziton.movielocker;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.store.FilteredMovieStore;
import com.laziton.movielocker.store.FilteredMovieStore.IMovieStoreMonitor;

public class MovieListFragment extends ListFragment implements IMovieStoreMonitor {
	public static int EDIT_FILTER_CODE = 1;
	public static int EDIT_MOVIE_CODE = 2;
	
	private IDualPaneChannel channel = null;
	
	private ArrayList<Movie> movies;
	private MoviesAdapter moviesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		
		this.movies = FilteredMovieStore.getInstance().getMovies();
		this.moviesAdapter = new MoviesAdapter(this.movies);
		this.setListAdapter(moviesAdapter);
		this.setRetainInstance(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		if(activity instanceof IDualPaneChannel){
			this.channel = (IDualPaneChannel)activity;
		}
		FilteredMovieStore.getInstance().registerAdapter(this);
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		this.channel = null;
		FilteredMovieStore.getInstance().unRegisterAdapter(this);
		super.onDetach();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		ListView list = (ListView)v.findViewById(android.R.id.list);
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
        	this.registerForContextMenu(list);
        }
        else{
        	list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        	list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					MoviesAdapter adapter = (MoviesAdapter)getListAdapter();
					
					switch(item.getItemId()){
						case R.id.menu_delete:
							IDataService dataService = DataServiceFactory.getInstance().getDataService();
							dataService.open();
							
							for(int i = adapter.getCount() - 1; i >= 0; i--){
								if(getListView().isItemChecked(i)){
									dataService.deleteMovie(adapter.getItem(i));
								}
							}
							dataService.close();
							mode.finish();
							FilteredMovieStore.getInstance().updateStore();
							return true;
						case R.id.menu_edit:
							Movie movie = null;
							for(int i = adapter.getCount() - 1; i >= 0; i--){
								if(getListView().isItemChecked(i)){
									movie = adapter.getItem(i);
									break;
								}
							}
							Intent genreEdit = new Intent(getActivity(), MovieActivity.class);
					        genreEdit.putExtra(MovieActivity.MOVIE_ID, movie.getId());
					        startActivityForResult(genreEdit, EDIT_MOVIE_CODE);
					        mode.finish();
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
	public void onResume() {
		this.onStoreUpdated();
		super.onResume();
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.movie_list_menu, menu);
        menu.findItem(R.id.menu_done).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(false);
        this.getActivity().invalidateOptionsMenu();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_add:
				Intent genreAdd = new Intent(getActivity(), MovieActivity.class);
				startActivityForResult(genreAdd, MainActivity.ACTIVITY_CREATE_MOVIE);
				break;
			case R.id.menu_filter:
				Intent filterEdit = new Intent(MovieListFragment.this.getActivity(), MovieFilterActivity.class);
				MovieListFragment.this.startActivityForResult(filterEdit, MovieListFragment.EDIT_FILTER_CODE);
				break;
			case R.id.menu_collections:
				Intent collections = new Intent(getActivity(), CollectionListActivity.class);
				this.startActivity(collections);
				break;
			case R.id.menu_genres:
				Intent genreList = new Intent(getActivity(), GenreListActivity.class);
				this.startActivity(genreList);
				break;
		}
		
		return true;
	}
	
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	this.getActivity().getMenuInflater().inflate(R.menu.movie_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		MoviesAdapter adapter = (MoviesAdapter)this.getListAdapter();
		Movie movie = adapter.getItem(position);
		
		switch(item.getItemId()){
			case R.id.menu_delete:
				IDataService dataService = DataServiceFactory.getInstance().getDataService();
				dataService.open();
				dataService.deleteMovie(movie);
				dataService.close();
				FilteredMovieStore.getInstance().updateStore();
				return true;
			case R.id.menu_edit:
				Intent genreEdit = new Intent(getActivity(), MovieActivity.class);
		        genreEdit.putExtra(MovieActivity.MOVIE_ID, movie.getId());
		        startActivityForResult(genreEdit, EDIT_MOVIE_CODE);
		        return true;
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == EDIT_FILTER_CODE){
			this.moviesAdapter.notifyDataSetChanged();
		} 
		else {
			if(resultCode == Activity.RESULT_OK){
				if(requestCode == MainActivity.ACTIVITY_CREATE_MOVIE){
					FilteredMovieStore.getInstance().updateStore();
				}
				else if(requestCode == EDIT_MOVIE_CODE){
					FilteredMovieStore.getInstance().updateStore();
				}
			}
		}
	}

	public void onListItemClick(ListView listView, View view, int position, long id) {
		Movie movie = ((MoviesAdapter)getListAdapter()).getItem(position);
		
		if(this.channel != null && this.channel.hasRightPane()){
			Bundle args = new Bundle();
			args.putInt(MainActivity.SELECTED_MOVIE_POSITION, position);
			this.channel.callRightPane(args);
		}
		else{
	        Intent genreEdit = new Intent(getActivity(), MovieActivity.class);
	        genreEdit.putExtra(MovieActivity.MOVIE_ID, movie.getId());
	        startActivityForResult(genreEdit, 0);
		}
    }

	public class MoviesAdapter extends ArrayAdapter<Movie>{
		public MoviesAdapter(ArrayList<Movie> movies){
			super(getActivity(), android.R.layout.simple_list_item_1, movies);
		}

		@Override
		public int getPosition(Movie item) {
			return PagerAdapter.POSITION_NONE;
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

	@Override
	public void onStoreUpdated() {
		((MoviesAdapter)this.getListAdapter()).notifyDataSetChanged();
		this.getListView().invalidate();
	}
}