package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionActivity.CollectionFragment;
import com.laziton.movielocker.CollectionListActivity.CollectionListFragment.CollectionsAdapter;
import com.laziton.movielocker.IdMultiselectActivity.IdMultiselectFragment;
import com.laziton.movielocker.IdMultiselectActivity.KeyValueSelection;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;

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

public class CollectionMembersActivity extends IdMultiselectActivity {
	public static String EXTRA_COLLECTION_ID = "collectionId";
	public static int EDIT_FILTER_CODE = 1;
	
	@Override
	protected Fragment createFragment() {
		int collectionId = getIntent().getIntExtra(EXTRA_COLLECTION_ID, 0);
		return CollectionMembersFragment.newInstance(collectionId);
	}
	
	public static class CollectionMembersFragment extends IdMultiselectFragment{
		private int collectionId;
		
		public static CollectionMembersFragment newInstance(int collectionId){
			CollectionMembersFragment result = new CollectionMembersFragment();
			
			result.collectionId = collectionId;
			result.selections = new ArrayList<KeyValueSelection>();
			result.populateSelections(false);
			
			return result;
		}
		
		private void populateSelections(boolean notify){
			this.selections.clear();
			ArrayList<Integer> collectionMovies = new ArrayList<Integer>();
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			IFilteredMovieDataService filteredService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
			filteredService.Open();
			for(CollectionMovie member : dataService.GetCollectionMovies(collectionId, null)){
				collectionMovies.add(member.getMovieId());
			}
			for(Movie movie : filteredService.GetFilteredMovies()){
				KeyValueSelection selection = new KeyValueSelection();
				selection.id = movie.getId();
				selection.name = movie.getName();
				selection.selected = collectionMovies.contains(movie.getId());
				this.selections.add(selection);
			}
			
			if(notify){
				ListView lv = this.getListView();
				lv.clearChoices();
				this.selectionsAdapter.notifyDataSetChanged();
				
				for(KeyValueSelection selection : this.selections){
					if(selection.selected)
						lv.setItemChecked(this.selections.indexOf(selection), true);
				}
			}
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        menu.findItem(R.id.menu_filter).setVisible(true);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.menu_filter:
					Intent filterEdit = new Intent(CollectionMembersFragment.this.getActivity(), MovieFilterActivity.class);
					CollectionMembersFragment.this.startActivityForResult(filterEdit, CollectionMembersActivity.EDIT_FILTER_CODE);
	            	break;
				default:
					return super.onOptionsItemSelected(item);
			}
			
			return true;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(requestCode == CollectionMembersActivity.EDIT_FILTER_CODE){
				this.populateSelections(true);
			}
		}
	}

}
