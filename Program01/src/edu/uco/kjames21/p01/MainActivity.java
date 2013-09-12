package edu.uco.kjames21.p01;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	public static final int GET_CONTACTS_RESULT = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnContacts = (Button)this.findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent contactsIntent = new Intent(MainActivity.this, ContactsActivity.class);
				startActivity(contactsIntent);
			}
		});
        
        ImageButton btnUCO = (ImageButton)this.findViewById(R.id.btnUCO);
        btnUCO.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri ucoUri = Uri.parse("http://www.uco.edu");
				Intent ucoIntent = new Intent(Intent.ACTION_VIEW, ucoUri);
				startActivity(ucoIntent);
			}
		});
        
        Button btnCS = (Button)this.findViewById(R.id.btnCS);
        btnCS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri ucoUri = Uri.parse("http://cs.uco.edu");
				Intent ucoIntent = new Intent(Intent.ACTION_VIEW, ucoUri);
				startActivity(ucoIntent);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
