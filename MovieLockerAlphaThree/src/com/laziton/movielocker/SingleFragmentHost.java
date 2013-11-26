package com.laziton.movielocker;

import com.laziton.mlalphathree.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public abstract class SingleFragmentHost extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_fragment_host);
		
		FragmentManager manager = this.getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			fragment = this.createFragment();
			manager.beginTransaction()
					.add(R.id.fragmentContainer, fragment)
					.commit();
		}
	}

	protected abstract Fragment createFragment();
}