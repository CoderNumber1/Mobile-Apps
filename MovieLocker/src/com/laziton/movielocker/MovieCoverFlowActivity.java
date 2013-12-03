package com.laziton.movielocker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

public class MovieCoverFlowActivity extends FragmentActivity {

	ViewPager pager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = new MovieCoverFlowViewPager(this);
        pager.setId(R.id.viewPager);
        setContentView(pager);

        FragmentManager manager = getSupportFragmentManager();
        pager.setAdapter(new MovieCoverFlowViewPager.CoverFlowAdapter(manager));
	}
}
