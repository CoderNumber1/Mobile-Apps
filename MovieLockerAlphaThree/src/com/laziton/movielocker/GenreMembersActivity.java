package com.laziton.movielocker;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionMembersActivity.CollectionMembersFragment;
import com.laziton.movielocker.IdMultiselectActivity.IdMultiselectFragment;
import com.laziton.movielocker.IdMultiselectActivity.KeyValueSelection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;

public class GenreMembersActivity extends IdMultiselectActivity {
	public static String EXTRA_GENRE_ID = "genreId";
	public static int EDIT_FILTER_CODE = 1;
	
	@Override
	protected Fragment createFragment() {
		int genreId = getIntent().getIntExtra(EXTRA_GENRE_ID, 0);
		return GenreMembersFragment.newInstance(genreId);
	}
	
	public static class GenreMembersFragment extends IdMultiselectFragment{
		private Integer genreId;
		
		public static GenreMembersFragment newInstance(int genreId){
			GenreMembersFragment result = new GenreMembersFragment();
			
			result.genreId = genreId;
			result.selections = new ArrayList<KeyValueSelection>();
			result.populateSelections(false);
			
			return result;
		}
		
		private void populateSelections(boolean notify){
			this.selections.clear();
			ArrayList<Integer> genreMovies = new ArrayList<Integer>();
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
			dataService.Open();
			IFilteredMovieDataService filteredService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
			filteredService.Open();
			MovieFilter filter = new MovieFilter();
			filter.setGenreIds(this.genreId.toString());
			for(Movie movie : dataService.GetMoviesByFilter(filter)){
				genreMovies.add(movie.getId());
			}
			for(Movie movie : filteredService.GetFilteredMovies()){
				KeyValueSelection selection = new KeyValueSelection();
				selection.id = movie.getId();
				selection.name = movie.getName();
				selection.selected = genreMovies.contains(movie.getId());
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
					Intent filterEdit = new Intent(GenreMembersFragment.this.getActivity(), MovieFilterActivity.class);
					GenreMembersFragment.this.startActivityForResult(filterEdit, CollectionMembersActivity.EDIT_FILTER_CODE);
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
