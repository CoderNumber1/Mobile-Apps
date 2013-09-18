package edu.uco.kjames21.p03;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Fragment {
	
	Map<String,String> users;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		users = new HashMap<String,String>();
		users.put("Amy", "1234");
		users.put("Mark", "5678");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		
		Button btnLogin = (Button)v.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TextView txtErrorLabel = (TextView)Login.this.getView().findViewById(R.id.txtErrorLabel);
				EditText txtUserId = (EditText)Login.this.getView().findViewById(R.id.txtUserId);
				EditText txtPassword = (EditText)Login.this.getView().findViewById(R.id.txtPassword);
				
				if(txtPassword.getText().toString().equals("")
						|| !Login.this.users.containsKey(txtUserId.getText().toString())
						|| !Login.this.users.get(txtUserId.getText().toString()).endsWith(txtPassword.getText().toString())){
					txtErrorLabel.setVisibility(View.VISIBLE);
					return;
				}
				
				txtErrorLabel.setVisibility(View.GONE);
				Intent welcome = new Intent(getActivity(), WelcomeActivity.class);
				welcome.putExtra(WelcomeActivity.USER_NAME_FIELD, txtUserId.getText().toString());
				startActivity(welcome);
			}
		});
		
		return v;
	}

}
