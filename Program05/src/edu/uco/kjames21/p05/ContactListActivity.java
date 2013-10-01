package edu.uco.kjames21.p05;

import android.support.v4.app.Fragment;

public class ContactListActivity extends FragmentHostActivity {

    @Override
    protected Fragment createFragment() {
        return new ContactListFragment();
    }
}