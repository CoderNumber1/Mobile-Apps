package edu.uco.kjames21.p01;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;

public class ContactsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
		String sort = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		
		CursorLoader loader = new CursorLoader(ContactsActivity.this, contactUri, null, null, null, sort);
		Cursor contactCursor = loader.loadInBackground();
		String[] displayColumns = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
		int[] viewIds = new int[] {android.R.id.text1};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(ContactsActivity.this, android.R.layout.simple_list_item_1, contactCursor, displayColumns, viewIds, SimpleCursorAdapter.NO_SELECTION);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

}
