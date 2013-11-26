package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionMembersActivity.CollectionMembersFragment;
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

public class MovieCollectionsActivity extends SingleFragmentHost {
	public static String EXTRA_SELECTED_COLLECTIONS = "selectedCollections";

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(MovieActivity.MOVIE_ID, -1);
		return MovieCollectionsFragment.newInstance(id);
	}
	
	public static class MovieCollectionsFragment extends ListFragment{
		private ArrayList<Collection> collections;
		private ArrayAdapter<Collection> collectionAdapter;
		
		public static MovieCollectionsFragment newInstance(int movieId){
			MovieCollectionsFragment result = new MovieCollectionsFragment();
			Bundle args = new Bundle();
			args.putInt(MovieActivity.MOVIE_ID, movieId);
			result.setArguments(args);
			return result;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			this.collections = dataService.GetCollections();
			dataService.Close();
			
			this.collectionAdapter = new ArrayAdapter<Collection>(getActivity(), android.R.layout.simple_list_item_multiple_choice, this.collections){
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            if (null == convertView) {
		                convertView = getActivity().getLayoutInflater()
		                    .inflate(android.R.layout.simple_list_item_multiple_choice, null);
		            }

		            Collection collection = getItem(position);
		            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
		            titleTextView.setText(collection.getName());

		            return convertView;
				}
			};
			
			this.setRetainInstance(true);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			this.setListAdapter(this.collectionAdapter);
			ListView lv = this.getListView();
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			for(CollectionMovie entry : dataService.GetCollectionMovies(null, this.getArguments().getInt(MovieActivity.MOVIE_ID))){
				for(Collection collection : this.collections){
					if(entry.getCollectionId() == collection.getId())
						lv.setItemChecked(this.collections.indexOf(collection), true);
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
			        ArrayList<Collection> selectedItems = new ArrayList<Collection>();
			        for (int i = 0; i < checked.size(); i++) {
			            int position = checked.keyAt(i);
			            if (checked.valueAt(i))
			                selectedItems.add(this.collectionAdapter.getItem(position));
			        }

					Intent i = new Intent();
					Bundle args = new Bundle();
					args.putSerializable(EXTRA_SELECTED_COLLECTIONS, selectedItems);
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
	}
}
