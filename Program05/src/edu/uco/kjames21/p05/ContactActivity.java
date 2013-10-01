package edu.uco.kjames21.p05;

import java.util.UUID;
import android.support.v4.app.Fragment;

public class ContactActivity extends FragmentHostActivity {
	@Override
    protected Fragment createFragment() {
        UUID id = (UUID)getIntent()
            .getSerializableExtra(ContactFragment.CONTACT_ID);
        return ContactFragment.newInstance(id);
    }
}
