package edu.uco.kjames21.p08;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;

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
}
