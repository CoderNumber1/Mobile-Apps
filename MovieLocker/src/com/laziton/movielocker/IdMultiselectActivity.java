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

public class IdMultiselectActivity extends SingleFragmentHost {
	public static String EXTRA_SELECTED_IDS = "selectedIds";
	public static String SELECTIONS = "keyValueSelections";
	
	@SuppressWarnings("unchecked")
	@Override
	protected Fragment createFragment() {
		Object selectionsObject = getIntent().getSerializableExtra(IdMultiselectActivity.SELECTIONS);
		ArrayList<KeyValueSelection> selections = (ArrayList<KeyValueSelection>)selectionsObject;
		return IdMultiselectFragment.newInstance(selections);
	}
	
	public static class KeyValueSelection implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public KeyValueSelection(){}
		
		public int id;
		public String name;
		public boolean selected;
	}
	
	public static class IdMultiselectFragment extends ListFragment{
		protected ArrayList<KeyValueSelection> selections;
		protected ArrayAdapter<KeyValueSelection> selectionsAdapter;
		
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
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
			
			View result = super.onCreateView(inflater, container, savedInstanceState);
			return result;
		}
		
		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.id_multiselect_menu, menu);
	        menu.findItem(R.id.menu_add).setVisible(false);
	        menu.findItem(R.id.menu_save).setVisible(false);
	        menu.findItem(R.id.menu_filter).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.menu_done:
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
					getActivity().setResult(Activity.RESULT_OK, i);
					
					getActivity().finish();
					
					break;
				case R.id.menu_select_all:
					for(KeyValueSelection selection : this.selections){
						this.getListView().setItemChecked(this.selections.indexOf(selection), true);
					}
					break;
				case R.id.menu_select_none:
					for(KeyValueSelection selection : this.selections){
						this.getListView().setItemChecked(this.selections.indexOf(selection), false);
					}
					break;
				case android.R.id.home:
	                getActivity().setResult(RESULT_CANCELED);
	                getActivity().finish();
	                return true;
			}
			
			return true;
		}
	}
}