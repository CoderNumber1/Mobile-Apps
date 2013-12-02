package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionMembersActivity.CollectionMembersFragment;
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

public class MovieCollectionsActivity extends IdMultiselectActivity {
	public static String EXTRA_SELECTED_COLLECTIONS = "selectedCollections";
	public static String EXTRA_MOVIE_ID = "movieId";

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
		return MovieCollectionsFragment.newInstance(id);
	}
	
	public static class MovieCollectionsFragment extends IdMultiselectFragment{
		private int movieId;
		
		public static MovieCollectionsFragment newInstance(int movieId){
			MovieCollectionsFragment result = new MovieCollectionsFragment();
			result.movieId = movieId;
			result.selections = new ArrayList<KeyValueSelection>();
			result.populateSelections(false);
			
			return result;
		}
		
		private void populateSelections(boolean notify){
			this.selections.clear();
			ArrayList<Integer> movieCollections = new ArrayList<Integer>();
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			for(CollectionMovie member : dataService.GetCollectionMovies(null, this.movieId)){
				movieCollections.add(member.getCollectionId());
			}
			
			for(Collection collection : dataService.GetCollections()){
				KeyValueSelection selection = new KeyValueSelection();
				selection.id = collection.getId();
				selection.name = collection.getName();
				selection.selected = movieCollections.contains(collection.getId());
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
	}
}
