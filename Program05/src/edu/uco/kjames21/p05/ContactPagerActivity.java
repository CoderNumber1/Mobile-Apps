package edu.uco.kjames21.p05;

import java.util.ArrayList;
import java.util.UUID;

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

        final ArrayList<Contact> contacts = ContactDataService.get(this).getContacts();

        FragmentManager manager = getSupportFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public int getCount() {
                return contacts.size();
            }
            @Override
            public Fragment getItem(int pos) {
                UUID id =  contacts.get(pos).getId();
                return ContactFragment.newInstance(id);
            }
        }); 

        UUID crimeId = (UUID)getIntent().getSerializableExtra(ContactFragment.CONTACT_ID);
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(crimeId)) {
                pager.setCurrentItem(i);
                break;
            } 
        }
    }
}