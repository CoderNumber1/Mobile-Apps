package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionActivity.CollectionFragment;
import com.laziton.movielocker.CollectionListActivity.CollectionListFragment.CollectionsAdapter;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionMembersActivity extends SingleFragmentHost {
	public static String EXTRA_SELECTED_MOVIES = "selectedMovies";
	
	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(CollectionActivity.COLLECTION_ID, -1);
		return CollectionMembersFragment.newInstance(id);
	}
	
	public static class CollectionMembersFragment extends ListFragment{
		private ArrayList<Movie> movies;
		private ArrayAdapter<Movie> movieAdapter;
		
		public static CollectionMembersFragment newInstance(int collectionId){
			CollectionMembersFragment result = new CollectionMembersFragment();
			Bundle args = new Bundle();
			args.putInt(CollectionActivity.COLLECTION_ID, collectionId);
			result.setArguments(args);
			return result;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			this.movies = dataService.GetMovies();
			dataService.Close();
			
			this.movieAdapter = new ArrayAdapter<Movie>(getActivity(), android.R.layout.simple_list_item_multiple_choice, this.movies){
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            if (null == convertView) {
		                convertView = getActivity().getLayoutInflater()
		                    .inflate(android.R.layout.simple_list_item_multiple_choice, null);
		            }

		            Movie movie = getItem(position);
		            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
		            titleTextView.setText(movie.getName());

		            return convertView;
				}
			};
			
			this.setRetainInstance(true);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			this.setListAdapter(this.movieAdapter);
			ListView lv = this.getListView();
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			for(CollectionMovie entry : dataService.GetCollectionMovies(this.getArguments().getInt(CollectionActivity.COLLECTION_ID), null)){
				for(Movie movie : this.movies){
					if(entry.getMovieId() == movie.getId())
						lv.setItemChecked(this.movies.indexOf(movie), true);
				}
			}
			dataService.Close();
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
			
			View result = super.onCreateView(inflater, container, savedInstanceState);
			return result;
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.crud_option_menu, menu);
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.genre_menu_save:
					SparseBooleanArray checked = this.getListView().getCheckedItemPositions();
			        ArrayList<Movie> selectedItems = new ArrayList<Movie>();
			        for (int i = 0; i < checked.size(); i++) {
			            int position = checked.keyAt(i);
			            if (checked.valueAt(i))
			                selectedItems.add(this.movieAdapter.getItem(position));
			        }

					Intent i = new Intent();
//					i.putExtra(EXTRA_SELECTED_MOVIES, selectedItems);
					Bundle args = new Bundle();
					args.putSerializable(EXTRA_SELECTED_MOVIES, selectedItems);
					i.putExtras(args);
					getActivity().setResult(Activity.RESULT_OK, i);
					
					getActivity().finish();
					
					break;
				case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
			}
			
			return true;
		}
		
//		public void onListItemClick(ListView listView, View view, int position, long id) {
//	        Collection collection = ((CollectionsAdapter)getListAdapter()).getItem(position);
//	        Intent collectionEdit = new Intent(getActivity(), CollectionActivity.class);
//	        collectionEdit.putExtra(CollectionActivity.COLLECTION_ID, collection.getId());
//	        startActivityForResult(collectionEdit, 0);
//	    }
	}

}
