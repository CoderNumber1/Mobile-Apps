package edu.uco.kjames21.p06;

import android.support.v4.app.Fragment;

public class ContactListActivity extends FragmentHostActivity {

    @Override
    protected Fragment createFragment() {
        return new ContactListFragment();
    }
}