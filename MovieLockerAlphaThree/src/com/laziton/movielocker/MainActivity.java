package com.laziton.movielocker;

import com.laziton.mlalphathree.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnGenre = (Button)this.findViewById(R.id.btnGenre);
		btnGenre.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent genreList = new Intent(MainActivity.this, GenreListActivity.class);
				MainActivity.this.startActivity(genreList);
			}
		});
		
		Button btnMovie = (Button)this.findViewById(R.id.btnMovie);
		btnMovie.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent movieList = new Intent(MainActivity.this, MovieListActivity.class);
				MainActivity.this.startActivity(movieList);
			}
		});
		
		Button btnMovieCoverFlow = (Button)this.findViewById(R.id.btnCoverFlow);
		btnMovieCoverFlow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent movieList = new Intent(MainActivity.this, MovieCoverFlowActivity.class);
				MainActivity.this.startActivity(movieList);
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
