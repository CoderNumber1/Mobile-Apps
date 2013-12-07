package com.laziton.movielocker;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.laziton.movielocker.R;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

public class CollectionActivity extends SingleFragmentHost {
	public static String COLLECTION_ID = "collectionId";
	public static int GET_COLLECTION_MEMBERS = 1;

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(CollectionActivity.COLLECTION_ID, -1);
		return CollectionFragment.newInstance(id);
	}

	public static class CollectionFragment extends Fragment{
		Collection collection;
		ArrayList<Integer> collectionMovies;
		EditText txtName;
		Button btnEditMembers;

		public static CollectionFragment newInstance(int id){
			CollectionFragment result = new CollectionFragment();
			Bundle args = new Bundle();
	        args.putSerializable(CollectionActivity.COLLECTION_ID, id);
	        result.setArguments(args);
			return result;
		}
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        int id = getArguments().getInt(CollectionActivity.COLLECTION_ID);
	        this.collectionMovies = new ArrayList<Integer>();
	        if(id > 0){
		        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		        dataService.open();
		        this.collection = dataService.getCollection(id);
		        for(CollectionMovie member : dataService.getCollectionMovies(this.collection.getId(), null)){
		        	this.collectionMovies.add(member.getMovieId());
		        }
		        dataService.close();
	        }
	        else{
	        	this.collection = new Collection();
	        }
	        
	        setHasOptionsMenu(true);
	    }
		
		@TargetApi(11)
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.collection_fragment, parent, false);
	  
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }   
	        
	        txtName = (EditText)view.findViewById(R.id.txtName);
	        txtName.setText(collection.getName());
	        txtName.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	                collection.setName(c.toString());
	            }

	            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
	                // this space intentionally left blank
	            }

	            public void afterTextChanged(Editable c) {
	                // this one too
	            }
	        });
	        	        
	        this.btnEditMembers = (Button)view.findViewById(R.id.btnEditMembers);
	        this.btnEditMembers.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent getMembers = new Intent(getActivity(), CollectionMembersActivity.class);
					getMembers.putExtra(CollectionMembersActivity.EXTRA_COLLECTION_ID, collection.getId());
					CollectionFragment.this.startActivityForResult(getMembers, GET_COLLECTION_MEMBERS);
				}
			});
	        
	        return view; 
	    }
		
		@SuppressWarnings("unchecked")
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == Activity.RESULT_OK){
				if(requestCode == GET_COLLECTION_MEMBERS){
					Object selectionsObject = data.getSerializableExtra(IdMultiselectActivity.EXTRA_SELECTED_IDS);
					ArrayList<Integer> selectedIds = (ArrayList<Integer>)selectionsObject;
					this.collectionMovies = selectedIds;
				}
			}
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.main, menu);
	        menu.findItem(R.id.menu_done).setVisible(false);
	        menu.findItem(R.id.menu_add).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.open();
	    	
	        switch (item.getItemId()) {
	            case R.id.menu_save:
	            	if(this.collection.getId() > 0)
	            		dataService.updateCollection(this.collection);
	            	else
	            		dataService.insertCollection(this.collection);
	            	
	            	dataService.deleteCollectionMovies(dataService.getCollectionMovies(this.collection.getId(), null));
	            	for(Integer movieId : this.collectionMovies){
	            		CollectionMovie member = new CollectionMovie();
	            		member.setCollectionId(this.collection.getId());
	            		member.setMovieId(movieId);
	            		dataService.insertCollectionMovie(member);
	            	}
	            	
	            	dataService.close();
	            	getActivity().setResult(RESULT_OK);
	                getActivity().finish();
	            	break;
	            case R.id.menu_filter:
	            	Intent filterEdit = new Intent(CollectionFragment.this.getActivity(), MovieFilterActivity.class);
	            	CollectionFragment.this.startActivityForResult(filterEdit, 0);
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
