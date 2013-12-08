package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import android.support.v4.app.Fragment;
import android.widget.ListView;

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
			IDataService dataService = DataServiceFactory.getInstance().getDataService();
			dataService.open();
			for(CollectionMovie member : dataService.getCollectionMovies(null, this.movieId)){
				movieCollections.add(member.getCollectionId());
			}
			
			for(Collection collection : dataService.getCollections()){
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
