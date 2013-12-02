package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionActivity.CollectionFragment;
import com.laziton.movielocker.GenreListFragment.GenresAdapter;
import com.laziton.movielocker.MovieListActivity.MovieListFragment.MoviesAdapter;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
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

public class CollectionListActivity extends SingleFragmentHost {
	public static int ACTIVITY_CREATE_COLLECTION = 1;

	@Override
	protected Fragment createFragment() {
		return new CollectionListFragment();
	}

	public static class CollectionListFragment extends ListFragment {
		private ArrayList<Collection> collections;
		private CollectionsAdapter collectionsAdapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			this.collections = new ArrayList<Collection>();
			this.collectionsAdapter = new CollectionsAdapter(this.collections);
			this.setListAdapter(this.collectionsAdapter);
			this.setRetainInstance(true);
			this.populateList(false);
		}
		
		private void populateList(boolean notify){
			this.collections.clear();
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			this.collections.addAll(dataService.GetCollections());
			dataService.Close();
			
			if(notify)
				this.collectionsAdapter.notifyDataSetChanged();
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
								CollectionsAdapter adapter = (CollectionsAdapter)getListAdapter();
								IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
								dataService.Open();
								
								for(int i = adapter.getCount() - 1; i >= 0; i--){
									if(getListView().isItemChecked(i)){
										dataService.DeleteCollection(adapter.getItem(i));
									}
								}
								dataService.Close();
								mode.finish();
								populateList(true);
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
	        menu.findItem(R.id.menu_save).setVisible(false);
	        menu.findItem(R.id.menu_done).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.menu_add:
					Intent collectionAdd = new Intent(getActivity(), CollectionActivity.class);
					startActivityForResult(collectionAdd, CollectionListActivity.ACTIVITY_CREATE_COLLECTION);
					break;
				case R.id.menu_filter:
					Intent filterEdit = new Intent(CollectionListFragment.this.getActivity(), MovieFilterActivity.class);
					CollectionListFragment.this.startActivityForResult(filterEdit, 0);
					break;
				case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
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
			CollectionsAdapter adapter = (CollectionsAdapter)this.getListAdapter();
			Collection collection = adapter.getItem(position);
			
			switch(item.getItemId()){
				case R.id.menu_delete:
					IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
					dataService.Open();
					
					dataService.DeleteCollection(collection);
					dataService.Close();
					this.populateList(true);
					return true;
			}
			
			return super.onContextItemSelected(item);
		}
		
		public void onListItemClick(ListView listView, View view, int position, long id) {
	        Collection collection = ((CollectionsAdapter)getListAdapter()).getItem(position);
	        Intent collectionEdit = new Intent(getActivity(), CollectionActivity.class);
	        collectionEdit.putExtra(CollectionActivity.COLLECTION_ID, collection.getId());
	        startActivityForResult(collectionEdit, 0);
	    }

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == RESULT_OK){
				if(requestCode == CollectionListActivity.ACTIVITY_CREATE_COLLECTION){
					this.populateList(true);
				}
			}
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