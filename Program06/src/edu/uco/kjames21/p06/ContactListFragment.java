package edu.uco.kjames21.p06;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListFragment extends ListFragment {
    private ArrayList<Contact> contacts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.contact_list_title);
        
        ContactDataService.get(getActivity()).sortContacts(ContactComparator.COMPARE_MODE_LAST_NAME);
        contacts = ContactDataService.get(getActivity()).getContacts();
        
        ContactsAdapter adapter = new ContactsAdapter(contacts);
        setListAdapter(adapter);
        setRetainInstance(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        
        ListView list = (ListView)v.findViewById(android.R.id.list);
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
        	this.registerForContextMenu(list);
        }
        else{
        	list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        	list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch(item.getItemId()){
						case R.id.menu_item_delete_contact:
							ContactsAdapter adapter = (ContactsAdapter)getListAdapter();
							ContactDataService dataService = ContactDataService.get(getActivity());
							for(int i = adapter.getCount() - 1; i >= 0; i--){
								if(getListView().isItemChecked(i)){
									dataService.deleteContact(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							dataService.SaveContacts();
							return true;
						default:
							return false;
					}
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.contact_list_item_context, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) { }

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2, boolean arg3) { }
        	});
        }
        
        return v;
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        Contact contact = ((ContactsAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), ContactPagerActivity.class);
        i.putExtra(ContactFragment.CONTACT_ID, contact.getId());
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((ContactsAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_contact_menu, menu);
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	this.getActivity().getMenuInflater().inflate(R.menu.contact_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		ContactsAdapter adapter = (ContactsAdapter)this.getListAdapter();
		Contact contact = adapter.getItem(position);
		
		switch(item.getItemId()){
			case R.id.menu_item_delete_contact:
				ContactDataService dataService = ContactDataService.get(this.getActivity());
				dataService.deleteContact(contact);
				adapter.notifyDataSetChanged();
				dataService.SaveContacts();
				return true;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_contact:
                Contact contact = new Contact();
                ContactDataService.get(getActivity()).addContact(contact);
                Intent i = new Intent(getActivity(), ContactActivity.class);
                i.putExtra(ContactFragment.CONTACT_ID, contact.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_sort_name:
            	ContactDataService.get(getActivity()).sortContacts(ContactComparator.COMPARE_MODE_FIRST_NAME);
            	((ContactsAdapter)getListAdapter()).notifyDataSetChanged();
            	return true;
            case R.id.menu_item_sort_age:
            	ContactDataService.get(getActivity()).sortContacts(ContactComparator.COMPARE_MODE_AGE);
            	((ContactsAdapter)getListAdapter()).notifyDataSetChanged();
            default:
                return super.onOptionsItemSelected(item);
        } 
    }

	private class ContactsAdapter extends ArrayAdapter<Contact> {
        public ContactsAdapter(ArrayList<Contact> contacts) {
            super(getActivity(), android.R.layout.simple_list_item_1, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_contact, null);
            }

            // configure the view for this Crime
            Contact contact = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(R.id.name);
            titleTextView.setText(contact.getLastName() + " " + contact.getFirstName());

            return convertView;
        }
    }
}