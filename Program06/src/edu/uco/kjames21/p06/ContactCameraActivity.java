package edu.uco.kjames21.p06;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class ContactCameraActivity extends FragmentHostActivity {

	@Override
	protected Fragment createFragment() {
		return new ContactCameraFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	}
}
