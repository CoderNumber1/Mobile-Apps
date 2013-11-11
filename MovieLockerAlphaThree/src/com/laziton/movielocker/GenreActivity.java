package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class GenreActivity extends SingleFragmentHost {
	public static final String GENRE_ID = "com.laziton.movielocker.GenreActivity.genreId";

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(GenreActivity.GENRE_ID, -1);
		return GenreFragment.newInstance(id);
	}

	public static class GenreFragment extends Fragment{
		Genre genre;
		
		EditText txtName;
		
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
	        
	        return view; 
	    }
		
		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.crud_option_menu, menu);
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	
	        switch (item.getItemId()) {
	            case R.id.genre_menu_save:
	            	if(this.genre.getId() > 0)
	            		dataService.UpdateGenre(this.genre);
	            	else
	            		dataService.InsertGenre(this.genre);
	            	
	            	dataService.Close();
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
