package com.laziton.movielocker;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionMembersActivity.CollectionMembersFragment;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

public class IdMultiselectActivity extends SingleFragmentHost {
	public static String EXTRA_SELECTED_IDS = "selectedIds";
	public static String SELECTIONS = "keyValueSelections";
	
	@Override
	protected Fragment createFragment() {
		Object selectionsObject = getIntent().getSerializableExtra(IdMultiselectActivity.SELECTIONS);
		ArrayList<KeyValueSelection> selections = (ArrayList<KeyValueSelection>)selectionsObject;
		return IdMultiselectFragment.newInstance(selections);
	}
	
	public static class KeyValueSelection implements Serializable {
		public KeyValueSelection(){}
		
		public int id;
		public String name;
		public boolean selected;
	}
	
	public static class IdMultiselectFragment extends ListFragment{
		private ArrayList<KeyValueSelection> selections;
		private ArrayAdapter<KeyValueSelection> selectionsAdapter;
		
		public static IdMultiselectFragment newInstance(ArrayList<KeyValueSelection> selections){
			IdMultiselectFragment result = new IdMultiselectFragment();
			result.selections = selections;

			return result;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setHasOptionsMenu(true);
			
			this.selectionsAdapter = new ArrayAdapter<KeyValueSelection>(getActivity(), android.R.layout.simple_list_item_multiple_choice, this.selections){
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            if (null == convertView) {
		                convertView = getActivity().getLayoutInflater()
		                    .inflate(android.R.layout.simple_list_item_multiple_choice, null);
		            }

		            KeyValueSelection selection = getItem(position);
		            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
		            titleTextView.setText(selection.name);

		            return convertView;
				}
			};
			
			this.setRetainInstance(true);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			this.setListAdapter(this.selectionsAdapter);
			ListView lv = this.getListView();
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			
			for(KeyValueSelection selection : this.selections){
				if(selection.selected)
					lv.setItemChecked(this.selections.indexOf(selection), true);
			}
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//	        }
			
			View result = super.onCreateView(inflater, container, savedInstanceState);
			return result;
		}

		@Override
		public void onPause() {
			SparseBooleanArray checked = this.getListView().getCheckedItemPositions();
	        ArrayList<Integer> selectedItems = new ArrayList<Integer>();
	        for (int i = 0; i < checked.size(); i++) {
	            int position = checked.keyAt(i);
	            if (checked.valueAt(i)){
	            	
	                selectedItems.add(this.selectionsAdapter.getItem(position).id);
	            }
	        }

			Intent i = new Intent();
			i.putExtra(IdMultiselectActivity.EXTRA_SELECTED_IDS, selectedItems);
//			Bundle args = new Bundle();
//			args.putSerializable(IdMultiselectActivity.EXTRA_SELECTED_IDS, selectedItems);
//			i.putExtras(args);
			getActivity().setResult(Activity.RESULT_OK, i);
			
			getActivity().finish();
			
			super.onPause();
		}
	}
}