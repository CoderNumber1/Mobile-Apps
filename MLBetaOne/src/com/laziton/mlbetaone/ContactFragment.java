package com.laziton.mlbetaone;

import com.laziton.mlbetaone.data.Movie;
import com.laziton.mlbetaone.dataservices.DataServiceFactory;
import com.laziton.mlbetaone.dataservices.IDataService;

import edu.uco.kjames21.p06.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ContactFragment extends Fragment {
    public static final String MOVIE_ID = "edu.uco.kjames21.ContactFragment.CrimeId";
    public static final String DIALOG_IMAGE = "edu.uco.kjames21.ContactFragment.DialogImage";
    public static final int REQUEST_PHOTO = 1;

    Movie contact;
    
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
}