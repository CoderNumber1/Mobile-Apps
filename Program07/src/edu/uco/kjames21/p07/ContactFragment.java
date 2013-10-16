package edu.uco.kjames21.p07;

import java.util.UUID;

import edu.uco.kjames21.p07.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ContactFragment extends Fragment {
    public static final String CONTACT_ID = "edu.uco.kjames21.ContactFragment.CrimeId";
    public static final String DIALOG_IMAGE = "edu.uco.kjames21.ContactFragment.DialogImage";
    public static final int REQUEST_PHOTO = 1;
    
    private IContactCallbacks callbacks;

    Contact contact;
    
    ImageView imgContactPhoto;
    ImageButton btnTakeContactPicture;
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
        
        imgContactPhoto = (ImageView)view.findViewById(R.id.imgContactPhoto);
        imgContactPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String photoName = contact.getPhoto();
				if(photoName == null)
					return;
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(photoName).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
        
        btnTakeContactPicture = (ImageButton)view.findViewById(R.id.btnTakeContactPicture);
        btnTakeContactPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), ContactCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
        
        PackageManager pm = getActivity().getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        	&& !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
        		btnTakeContactPicture.setEnabled(false);
        	}
        
        txtFirstName = (EditText)view.findViewById(R.id.txtFirstName);
        txtFirstName.setText(contact.getFirstName());
        txtFirstName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                contact.setFirstName(c.toString());
                callbacks.OnContactUpdated(contact);
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
                callbacks.OnContactUpdated(contact);
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
            	callbacks.OnContactUpdated(contact);
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
                callbacks.OnContactUpdated(contact);
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
                callbacks.OnContactUpdated(contact);
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
		ContactDataService.get(getActivity()).SaveContacts();
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
		
		if(requestCode == ContactFragment.REQUEST_PHOTO){
			String fileName = data.getStringExtra(ContactCameraFragment.EXTRA_PHOTO_FILENAME);
			if(fileName != null){
				this.contact.setPhoto(fileName);
				this.showPhoto();
				callbacks.OnContactUpdated(contact);
			}
		}
	}	
	
	private void showPhoto(){
		String photoName = this.contact.getPhoto();
		BitmapDrawable b = null;
		if(photoName != null && !photoName.equals("")){
			String path = getActivity().getFileStreamPath(photoName).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		
		this.imgContactPhoto.setImageDrawable(b);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		this.showPhoto();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		PictureUtils.cleanImageView(this.imgContactPhoto);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.callbacks = (IContactCallbacks)activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		this.callbacks = null;
	}

	public interface IContactCallbacks{
		void OnContactUpdated(Contact contact);
	}
}