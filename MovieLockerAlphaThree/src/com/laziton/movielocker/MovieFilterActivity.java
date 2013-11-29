package com.laziton.movielocker;

import java.util.ArrayList;
import com.laziton.mlalphathree.R;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.MovieFilter;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MovieFilterActivity extends SingleFragmentHost {
	public static final int GET_COLLECTIONS_CODE = 1;
	public static final int GET_GENRES_CODE = 2;
	
	@Override
	protected Fragment createFragment() {
		return MovieFilterFragment.newInstance();
	}

	public static class MovieFilterFragment extends Fragment{
		MovieFilter filter;
		
		EditText txtName;
		Button btnSelectCollections;
		Button btnSelectGenres;
		CheckBox chkOwned;
		CheckBox chkWishlist;

		public static MovieFilterFragment newInstance(){
			MovieFilterFragment result = new MovieFilterFragment();

			return result;
		}
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        IFilteredMovieDataService filterService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
	        
	        filterService.Open();
	        this.filter = filterService.GetMovieFilter();
	        filterService.Close();
	        
	        setHasOptionsMenu(true);
	    }
		
		@TargetApi(11)
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.movie_filter_fragment, parent, false);
	  
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }   
	        
	        this.txtName = (EditText)view.findViewById(R.id.txtName);
	        this.txtName.setText(this.filter.getMovieName());
	        this.txtName.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	                filter.setMovieName(c.toString());
	            }

	            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
	                // this space intentionally left blank
	            }

	            public void afterTextChanged(Editable c) {
	                // this one too
	            }
	        });
	        	        
	        this.btnSelectCollections = (Button)view.findViewById(R.id.btnSelectCollections);
	        this.btnSelectCollections.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent getCollections = new Intent(getActivity(), IdMultiselectActivity.class);
					IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
					dataService.Open();
					ArrayList<IdMultiselectActivity.KeyValueSelection> selections = new ArrayList<IdMultiselectActivity.KeyValueSelection>();
					ArrayList<Integer> selectedCollections = new ArrayList<Integer>();
					if(!filter.getCollectionIds().equals(null) && !filter.getCollectionIds().equals("")){
			        	for(String collection : filter.getCollectionIds().split(",")){
			        		selectedCollections.add(Integer.parseInt(collection));
			        	}
			        }
					
					for(Collection collection : dataService.GetCollections()){
						IdMultiselectActivity.KeyValueSelection selection = new IdMultiselectActivity.KeyValueSelection();
						selection.name = collection.getName();
						selection.id = collection.getId();
						selection.selected = selectedCollections.contains(collection.getId());
						selections.add(selection);
					}
					
					getCollections.putExtra(IdMultiselectActivity.SELECTIONS, selections);
					MovieFilterFragment.this.startActivityForResult(getCollections, GET_COLLECTIONS_CODE);
				}
			});
	        
	        this.btnSelectGenres = (Button)view.findViewById(R.id.btnSelectGenres);
	        this.btnSelectGenres.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent getGenres = new Intent(getActivity(), IdMultiselectActivity.class);
					IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
					dataService.Open();
					ArrayList<IdMultiselectActivity.KeyValueSelection> selections = new ArrayList<IdMultiselectActivity.KeyValueSelection>();
					ArrayList<Integer> selectedGenres = new ArrayList<Integer>();
					if(!filter.getGenreIds().equals(null) && !filter.getGenreIds().equals("")){
			        	for(String genre : filter.getGenreIds().split(",")){
			        		selectedGenres.add(Integer.parseInt(genre));
			        	}
			        }
					
					for(Genre genre : dataService.GetGenres()){
						IdMultiselectActivity.KeyValueSelection selection = new IdMultiselectActivity.KeyValueSelection();
						selection.name = genre.getName();
						selection.id = genre.getId();
						selection.selected = selectedGenres.contains(genre.getId());
						selections.add(selection);
					}
					
					getGenres.putExtra(IdMultiselectActivity.SELECTIONS, selections);
					MovieFilterFragment.this.startActivityForResult(getGenres, GET_GENRES_CODE);
				}
			});
			
			this.chkOwned = (CheckBox)view.findViewById(R.id.chkOwned);
			this.chkWishlist = (CheckBox)view.findViewById(R.id.chkWishList);
	        
	        this.chkOwned.setChecked(this.filter.isOwned());
	        this.chkWishlist.setChecked(this.filter.isWishList());
	        
	        this.chkOwned.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					filter.setOwned(((CheckBox)v).isChecked());
				}
			});
			
			this.chkWishlist.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					filter.setWishList(((CheckBox)v).isChecked());
				}
			});
	        
	        return view; 
	    }
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == RESULT_OK){
				if(requestCode == MovieFilterActivity.GET_COLLECTIONS_CODE){
					Object selectionsObject = data.getSerializableExtra(IdMultiselectActivity.EXTRA_SELECTED_IDS);
					ArrayList<Integer> selectedIds = (ArrayList<Integer>)selectionsObject;
					
					StringBuilder sb = new StringBuilder();
					for(Integer id : selectedIds){
						sb.append(id.toString() + ",");
					}
					this.filter.setCollectionIds(sb.substring(0, sb.length() - 2));
				}
				else if(requestCode == MovieFilterActivity.GET_GENRES_CODE){
					Object selectionsObject = data.getSerializableExtra(IdMultiselectActivity.EXTRA_SELECTED_IDS);
					ArrayList<Integer> selectedIds = (ArrayList<Integer>)selectionsObject;
					
					StringBuilder sb = new StringBuilder();
					for(Integer id : selectedIds){
						sb.append(id.toString() + ",");
					}
					this.filter.setGenreIds(sb.substring(0, sb.length() - 2));
				}
			}
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.crud_option_menu, menu);
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.genre_menu_save:
	            	IFilteredMovieDataService filterService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
	            	filterService.SetMovieFilter(this.filter);
	            	break;
	            case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	        
	        return true;
	    }
	}
}
