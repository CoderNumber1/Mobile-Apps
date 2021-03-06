package com.laziton.movielocker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.MovieActivity.MovieFragment;
import com.laziton.movielocker.MovieActivity.MovieFragment.BarcodeSearchAsyncCallback;
import com.laziton.movielocker.MovieActivity.MovieFragment.BarcodeSearchResult;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class GenreActivity extends SingleFragmentHost {
	public static final String GENRE_ID = "com.laziton.movielocker.GenreActivity.genreId";
	public static int EDIT_GENRE_MOVIES = 1;

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(GenreActivity.GENRE_ID, -1);
		return GenreFragment.newInstance(id);
	}

	public static class GenreFragment extends Fragment{
		Genre genre;
		ArrayList<Integer> movies;
		EditText txtName;
		Button btnEditMovies;
		
		public static GenreFragment newInstance(int id){
			GenreFragment result = new GenreFragment();
			Bundle args = new Bundle();
	        args.putSerializable(GenreActivity.GENRE_ID, id);
	        result.setArguments(args);
			return result;
		}
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        int id = getArguments().getInt(GenreActivity.GENRE_ID);
	        if(id > 0){
		        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		        dataService.Open();
		        this.genre = dataService.GetGenre(id);
		        dataService.Close();
	        }
	        else{
	        	this.genre = new Genre();
	        }
	        
	        setHasOptionsMenu(true);
	    }
		
		@TargetApi(11)
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.genre_fragment, parent, false);
	  
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }   
	        
	        txtName = (EditText)view.findViewById(R.id.txtName);
	        txtName.setText(genre.getName());
	        txtName.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	                genre.setName(c.toString());
	            }

	            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
	                // this space intentionally left blank
	            }

	            public void afterTextChanged(Editable c) {
	                // this one too
	            }
	        });
	        
	        this.btnEditMovies = (Button)view.findViewById(R.id.btnEditMovies);
	        this.btnEditMovies.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent getMembers = new Intent(getActivity(), GenreMembersActivity.class);
					getMembers.putExtra(GenreMembersActivity.EXTRA_GENRE_ID, genre.getId());
					GenreFragment.this.startActivityForResult(getMembers, GenreActivity.EDIT_GENRE_MOVIES);
				}
			});
	        
	        return view; 
	    }
		
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == RESULT_OK){
				if(requestCode == GenreActivity.EDIT_GENRE_MOVIES){
					Object selectionsObject = data.getSerializableExtra(IdMultiselectActivity.EXTRA_SELECTED_IDS);
					ArrayList<Integer> selectedIds = (ArrayList<Integer>)selectionsObject;
					this.movies = selectedIds;
				}
			}
		}
		
		
		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.main, menu);
	        menu.findItem(R.id.menu_add).setVisible(false);
	        menu.findItem(R.id.menu_done).setVisible(false);
	        menu.findItem(R.id.menu_filter).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	
	        switch (item.getItemId()) {
	            case R.id.menu_save:
	            	if(this.genre.getId() > 0)
	            		dataService.UpdateGenre(this.genre);
	            	else
	            		dataService.InsertGenre(this.genre);
	            	
	            	for(Movie movie : dataService.GetMovies(this.movies)){
	            		movie.setGenreId(this.genre.getId());
	            		dataService.UpdateMovie(movie);
	            	}
	            	
	            	dataService.Close();
	            	getActivity().setResult(RESULT_OK);
	                getActivity().finish();
	            	break;
	            case android.R.id.home:
	                getActivity().setResult(RESULT_CANCELED);
	                getActivity().finish();
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	        
	        return true;
	    }
	}
}
