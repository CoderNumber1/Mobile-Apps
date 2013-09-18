package edu.uco.kjames21.p03;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	public static final String USER_NAME_FIELD = "edu.uco.kjames21.UserName";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		String userName = getIntent().getStringExtra(USER_NAME_FIELD);
		TextView welcome = (TextView)this.findViewById(R.id.txtWelcomeUserLabel);
		welcome.setText(String.format(this.getString(R.string.txtWelcomeUserLabelText), userName));
		
		ImageView image = (ImageView)this.findViewById(R.id.imgUser);
		if(userName.equals("Amy")){
			image.setImageResource(R.drawable.amy);
		}
		else if(userName.equals("Mark")){
			image.setImageResource(R.drawable.mark);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
