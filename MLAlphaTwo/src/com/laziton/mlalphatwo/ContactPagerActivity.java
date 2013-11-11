package com.laziton.mlalphatwo;

import java.util.ArrayList;

import com.laziton.mlalphatwo.data.Movie;
import com.laziton.mlalphatwo.dataservices.DataServiceFactory;
import com.laziton.mlalphatwo.dataservices.IDataService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class ContactPagerActivity extends FragmentActivity {
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = new ViewPager(this);
        pager.setId(R.id.viewPager);
        setContentView(pager);

        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
        dataService.Open();
        final ArrayList<Movie> contacts = dataService.GetMovies();
        dataService.Close();

        FragmentManager manager = getSupportFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public int getCount() {
                return contacts.size();
            }
            @Override
            public Fragment getItem(int pos) {
                int id =  contacts.get(pos).getId();
                return ContactFragment.newInstance(id);
            }
        }); 

        int crimeId = getIntent().getIntExtra(ContactFragment.MOVIE_ID, -1);
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == crimeId) {
                pager.setCurrentItem(i);
                break;
            } 
        }
    }
}