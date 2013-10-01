package edu.uco.kjames21.p05;

import java.util.UUID;

import android.annotation.TargetApi;
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
    public static final String CONTACT_ID = "criminalintent.CRIME_ID";

    Contact contact;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtAge;
    EditText txtPhone;
    EditText txtEmail;

    public static ContactFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_ID, id);

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UUID id = (UUID)getArguments().getSerializable(CONTACT_ID);
        contact = ContactDataService.get(getActivity()).getContact(id);

        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, parent, false);
  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }   
        
        txtFirstName = (EditText)view.findViewById(R.id.txtFirstName);
        txtFirstName.setText(contact.getFirstName());
        txtFirstName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setFirstName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        txtLastName = (EditText)view.findViewById(R.id.txtLastName);
        txtLastName.setText(contact.getLastName());
        txtLastName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setLastName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        txtAge = (EditText)view.findViewById(R.id.txtAge);
        txtAge.setText(contact.getAge() != null ? contact.getAge().toString() : "");
        txtAge.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
            	if(!c.toString().equals("")){
            		contact.setAge(Integer.parseInt(c.toString()));
            	}
            	else{
            		contact.setAge(null);
            	}
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        txtPhone = (EditText)view.findViewById(R.id.txtPhone);
        txtPhone.setText(contact.getPhone());
        txtPhone.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setPhone(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        txtEmail = (EditText)view.findViewById(R.id.txtEmail);
        txtEmail.setText(contact.getEmail());
        txtEmail.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setEmail(c.toString());
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
}