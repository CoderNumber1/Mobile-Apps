package com.laziton.movielocker;

import java.util.ArrayList;

import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GenreListFragment extends ListFragment {
	private ArrayList<Genre> genres;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		
		IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		dataService.Open();
		this.genres = dataService.GetGenres();
		dataService.Close();
		
		GenresAdapter adapter = new GenresAdapter(this.genres);
		this.setListAdapter(adapter);
		this.setRetainInstance(true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(false);
        menu.findItem(R.id.menu_done).setVisible(false);
        menu.findItem(R.id.menu_filter).setVisible(false);
        this.getActivity().invalidateOptionsMenu();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_add:
				Intent genreAdd = new Intent(getActivity(), GenreActivity.class);
				startActivityForResult(genreAdd, 0);
				break;
			case android.R.id.home:
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
//                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
		}
		
		return true;
	}
	
	public void onListItemClick(ListView listView, View view, int position, long id) {
        Genre genre = ((GenresAdapter)getListAdapter()).getItem(position);
        Intent genreEdit = new Intent(getActivity(), GenreActivity.class);
        genreEdit.putExtra(GenreActivity.GENRE_ID, genre.getId());
        startActivityForResult(genreEdit, 0);
    }

	public class GenresAdapter extends ArrayAdapter<Genre>{
		public GenresAdapter(ArrayList<Genre> genres){
			super(getActivity(), android.R.layout.simple_list_item_1, genres);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(android.R.layout.simple_list_item_1, null);
            }

            Genre genre = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
            titleTextView.setText(genre.getName());

            return convertView;
		}
	}
}
