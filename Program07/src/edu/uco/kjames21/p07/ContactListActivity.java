package edu.uco.kjames21.p07;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ContactListActivity extends FragmentHostActivity 
								implements ContactListFragment.IContactCallbacks,
											ContactFragment.IContactCallbacks {

    @Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	@Override
    protected Fragment createFragment() {
        return new ContactListFragment();
    }

	@Override
	public void OnContactSelected(Contact contact) {
		if(this.findViewById(R.id.detailFragmentContainer) == null){
			Intent i = new Intent(this, ContactPagerActivity.class);
			i.putExtra(ContactFragment.CONTACT_ID, contact.getId());
			this.startActivity(i);
		}
		else{
			FragmentManager manager = this.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			Fragment oldContact = manager.findFragmentById(R.id.detailFragmentContainer);
			Fragment newContact = ContactFragment.newInstance(contact.getId());
			
			if(oldContact != null){
				transaction.remove(oldContact);
			}
			
			transaction.add(R.id.detailFragmentContainer, newContact);
			transaction.commit();
		}
	}

	@Override
	public void OnContactUpdated(Contact contact) {
		FragmentManager manager = this.getSupportFragmentManager();
		ContactListFragment list = (ContactListFragment)manager.findFragmentById(R.id.fragmentContainer);
		list.UpdateUI();
	}
}