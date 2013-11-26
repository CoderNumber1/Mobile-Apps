package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.GenreListFragment.GenresAdapter;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionListActivity extends SingleFragmentHost {

	@Override
	protected Fragment createFragment() {
		return new CollectionListFragment();
	}

	public static class CollectionListFragment extends ListFragment {
		private ArrayList<Collection> collections;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			this.collections = dataService.GetCollections();
			dataService.Close();
			
			CollectionsAdapter adapter = new CollectionsAdapter(this.collections);
			this.setListAdapter(adapter);
			this.setRetainInstance(true);
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
			
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.genre_option_menu, menu);
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.genre_menu_add:
					Intent collectionAdd = new Intent(getActivity(), CollectionActivity.class);
					startActivityForResult(collectionAdd, 0);
					break;
				case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
			}
			
			return true;
		}
		
		public void onListItemClick(ListView listView, View view, int position, long id) {
	        Collection collection = ((CollectionsAdapter)getListAdapter()).getItem(position);
	        Intent collectionEdit = new Intent(getActivity(), CollectionActivity.class);
	        collectionEdit.putExtra(CollectionActivity.COLLECTION_ID, collection.getId());
	        startActivityForResult(collectionEdit, 0);
	    }

		public class CollectionsAdapter extends ArrayAdapter<Collection>{
			public CollectionsAdapter(ArrayList<Collection> genres){
				super(getActivity(), android.R.layout.simple_list_item_1, genres);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
	            if (null == convertView) {
	                convertView = getActivity().getLayoutInflater()
	                    .inflate(android.R.layout.simple_list_item_1, null);
	            }

	            Collection collection = getItem(position);
	            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
	            titleTextView.setText(collection.getName());

	            return convertView;
			}
		}
	}
}