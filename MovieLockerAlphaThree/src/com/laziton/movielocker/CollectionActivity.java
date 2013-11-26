package com.laziton.movielocker;

import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Movie;
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
		ArrayList<Movie> collectionMovies;
		ArrayAdapter<Movie> moviesAdapter;
		EditText txtName;
		ListView lstMembers;
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
	        if(id > 0){
		        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		        dataService.Open();
		        this.collection = dataService.GetCollection(id);
		        this.collectionMovies = dataService.GetMoviesByCollectionMovies(dataService.GetCollectionMovies(this.collection.getId(), null));
		        dataService.Close();
	        }
	        else{
	        	this.collection = new Collection();
	        	this.collectionMovies = new ArrayList<Movie>();
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
	        
	        this.lstMembers = (ListView)view.findViewById(R.id.lstMembers);
	        moviesAdapter = new ArrayAdapter<Movie>(getActivity(), android.R.layout.simple_list_item_1, this.collectionMovies){
	        	@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            if (null == convertView) {
		                convertView = getActivity().getLayoutInflater()
		                    .inflate(android.R.layout.simple_list_item_1, null);
		            }

		            Movie movie = getItem(position);
		            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
		            titleTextView.setText(movie.getName());

		            return convertView;
				}
	        };
	        this.lstMembers.setAdapter(moviesAdapter);
	        
	        this.btnEditMembers = (Button)view.findViewById(R.id.btnEditMembers);
	        if(this.collection.getId() <= 0)
	        	this.btnEditMembers.setActivated(false);
	        this.btnEditMembers.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent getMembers = new Intent(getActivity(), CollectionMembersActivity.class);
					getMembers.putExtra(COLLECTION_ID, collection.getId());
					CollectionFragment.this.startActivityForResult(getMembers, GET_COLLECTION_MEMBERS);
				}
			});
	        
	        return view; 
	    }
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode != Activity.RESULT_OK){
				Log.i("Buggy", "Wrong result");
			}
			if(resultCode == Activity.RESULT_OK){
				if(requestCode == GET_COLLECTION_MEMBERS){
					Bundle args = data.getExtras();
					Object result = args.getSerializable(CollectionMembersActivity.EXTRA_SELECTED_MOVIES);
					ArrayList<Movie> members = (ArrayList<Movie>)result;
					IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
					dataService.Open();
					dataService.DeleteCollectionMovies(dataService.GetCollectionMovies(this.collection.getId(), null));
					for(Movie member : members){
						CollectionMovie memberEntry = new CollectionMovie();
						memberEntry.setCollectionId(this.collection.getId());
						memberEntry.setMovieId(member.getId());
						dataService.InsertCollectionMovie(memberEntry);
					}
					this.collectionMovies = dataService.GetMoviesByCollectionMovies(dataService.GetCollectionMovies(this.collection.getId(), null));
					this.moviesAdapter.notifyDataSetChanged();
					dataService.Close();
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
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	
	        switch (item.getItemId()) {
	            case R.id.genre_menu_save:
	            	if(this.collection.getId() > 0)
	            		dataService.UpdateCollection(this.collection);
	            	else
	            		dataService.InsertCollection(this.collection);
	            	
	            	dataService.Close();
	            	this.btnEditMembers.setActivated(true);
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
