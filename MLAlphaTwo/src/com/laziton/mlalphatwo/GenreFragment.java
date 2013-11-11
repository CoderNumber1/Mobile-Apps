package com.laziton.mlalphatwo;

import java.util.ArrayList;

import com.laziton.mlalphatwo.data.Genre;
import com.laziton.mlalphatwo.data.Movie;
import com.laziton.mlalphatwo.dataservices.DataServiceFactory;
import com.laziton.mlalphatwo.dataservices.IDataService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class GenreFragment extends Fragment {
	public static final String GENRE_ID = "com.laziton.mlalphatwo.GenreId";
    public static final String DIALOG_IMAGE = "edu.uco.kjames21.ContactFragment.DialogImage";
    public static final int REQUEST_PHOTO = 1;

    Genre genre;
    
    EditText txtName;
    EditText txtDescription;
    
    public static GenreFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putSerializable(GENRE_ID, id);

        GenreFragment fragment = new GenreFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int id = getArguments().getInt(GENRE_ID);
        Log.w("NewGenreFragment", String.valueOf(id));
        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
        dataService.Open();
        genre = dataService.GetGenre(id);
        dataService.Close();
        
        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, parent, false);
  
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } 
    }

	@Override
	public void onPause() {
		super.onPause();
		IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		dataService.Open();
		dataService.UpdateGenre(this.genre);
		dataService.Close();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
	}
}
