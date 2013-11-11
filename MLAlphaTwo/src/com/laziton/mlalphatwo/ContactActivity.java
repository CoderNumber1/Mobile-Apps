package com.laziton.mlalphatwo;

import android.support.v4.app.Fragment;

public class ContactActivity extends FragmentHostActivity {
	@Override
    protected Fragment createFragment() {
        int id = getIntent().getIntExtra(ContactFragment.MOVIE_ID, -1);
        return ContactFragment.newInstance(id);
    }
}
