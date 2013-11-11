package com.laziton.mlalphatwo;

import android.support.v4.app.Fragment;
import android.util.Log;

public class GenreActivity extends FragmentHostActivity {

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(GenreFragment.GENRE_ID, -1);
		Log.w("NewGenreFragment", String.valueOf(id));
        return GenreFragment.newInstance(id);
	}

}
