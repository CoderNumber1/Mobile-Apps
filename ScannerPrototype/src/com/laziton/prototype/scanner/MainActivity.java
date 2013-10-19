package com.laziton.prototype.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnScan = (Button)this.findViewById(R.id.button1);
		btnScan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try{
					URL url = new URL("http://api.upcdatabase.org/json/10c9750ae510b7c3393e5bfbe08d142f/049000026566");
					HttpURLConnection c = (HttpURLConnection)url.openConnection();
					c.setRequestMethod("GET");
					c.setRequestProperty("Content-Length", "0");
					c.setUseCaches(false);
					c.setAllowUserInteraction(false);
					c.connect();
					
					int status = c.getResponseCode();
					switch(status){
					case 200:
					case 201:
						BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
						StringBuilder sb = new StringBuilder();
						String line;
						while((line = br.readLine()) != null){
							sb.append(line + System.getProperty("line.separator"));
						}
						br.close();
						
						TextView t = (TextView)findViewById(R.id.textView1);
						t.setText(sb.toString());
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					
				}
//				Intent i = new Intent("com.google.zxing.client.android.SCAN");
//				i.putExtra("SCAN_MODE", "QR_CODE_MODE");
//				startActivity(i);
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
