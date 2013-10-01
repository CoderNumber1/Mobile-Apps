package edu.uco.kjames21.p05;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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