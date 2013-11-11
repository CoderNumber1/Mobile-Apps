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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ContactFragment extends Fragment {
    public static final String MOVIE_ID = "edu.uco.kjames21.ContactFragment.CrimeId";
    public static final String DIALOG_IMAGE = "edu.uco.kjames21.ContactFragment.DialogImage";
    public static final int REQUEST_PHOTO = 1;

    Movie contact;
    
    Spinner spnGenre;
    EditText txtName;
    EditText txtDescription;
    
    public static ContactFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putSerializable(MOVIE_ID, id);

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int id = getArguments().getInt(MOVIE_ID);
        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
        dataService.Open();
        contact = dataService.GetMovie(id);
        dataService.Close();
        
        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, parent, false);
  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }   
        
        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
        dataService.Open();
        ArrayList<Genre> genres = dataService.GetGenres();
        GenreAdapter adapter = new GenreAdapter(genres); 
        spnGenre = (Spinner)view.findViewById(R.id.spnGenre);
        spnGenre.setAdapter(adapter);
        for(Genre g : genres){if(g.getId() == contact.getGenreId()){spnGenre.setSelection(adapter.getPosition(g));break;}}
//        spnGenre.setSelection(adapter.getPosition(dataService.GetGenre(this.contact.getGenreId())));
        spnGenre.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int pos, long arg3) {
				Genre selectedGenre = (Genre)adapter.getItemAtPosition(pos);
				contact.setGenreId(selectedGenre.getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
        });
        
        txtName = (EditText)view.findViewById(R.id.txtName);
        txtName.setText(contact.getName());
        txtName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        txtDescription = (EditText)view.findViewById(R.id.txtDescription);
        txtDescription.setText(contact.getDescription());
        txtDescription.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setDescription(c.toString());
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
		dataService.UpdateMovie(this.contact);
		dataService.Close();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
	}
	
	private class GenreAdapter extends ArrayAdapter<Genre> {
        public GenreAdapter(ArrayList<Genre> contacts) {
            super(getActivity(), android.R.layout.simple_list_item_1, contacts);
        }

        @Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_contact, null);
            }

            // configure the view for this Crime
            Genre contact = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(R.id.name);
            titleTextView.setText(contact.getName());

            return convertView;
		}

		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_contact, null);
            }

            // configure the view for this Crime
            Genre contact = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(R.id.name);
            titleTextView.setText(contact.getName());

            return convertView;
        }
    }
}