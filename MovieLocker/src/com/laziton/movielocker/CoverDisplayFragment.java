package com.laziton.movielocker;

import com.laziton.movielocker.R;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.images.ImageManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CoverDisplayFragment extends Fragment {
	public static String MOVIE_ID = "movieId";
    
	int movieId;
	Movie movie;
    ImageView imgMovieCover;
    TextView txtName;

    public static CoverDisplayFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movieId);

        CoverDisplayFragment fragment = new CoverDisplayFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.movieId = getArguments().getInt(MOVIE_ID);
        IDataService dataService = DataServiceFactory.getInstance().getDataService();
        dataService.open();
        this.movie = dataService.getMovie(this.movieId);
        dataService.close();

        this.setImage(this.movieId);
        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_cover_display, parent, false);
  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }   
        
        imgMovieCover = (ImageView)view.findViewById(R.id.imgMovieCover);
        
        txtName = (TextView)view.findViewById(R.id.txtName);
        txtName.setText(movie.getName());
        
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

    private void setImage(int movieId){
		ImageManager.getInstance().getImageAsync(movieId, new ImageManager.GetImageAsyncCallback() {
			
			@Override
			public void callback(BitmapDrawable image) {
				if(image != null)
					CoverDisplayFragment.this.imgMovieCover.setImageDrawable(image);
			}
		});
	}
	
	@Override
	public void onStart(){
		super.onStart();
		this.setImage(this.movieId);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		ImageManager.getInstance().cleanImageView(this.imgMovieCover);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.setImage(this.movieId);
	}
}