package com.laziton.movielocker.coverflow;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.images.ImageManager;

public class ResourceImageAdapter extends BaseCoverFlowImageAdapter {

    private static final String TAG = ResourceImageAdapter.class.getSimpleName();
    private ArrayList<Movie> movies;
    private final Context context;

    public ResourceImageAdapter(final Context context, ArrayList<Movie> movies) {
        super();
        this.context = context;
        this.movies = movies;
    }

    @Override
    public synchronized int getCount() {
        return this.movies.size();
    }

    @Override
    protected Bitmap createBitmap(final int position) {
        Log.v(TAG, "creating item " + position);
        final Bitmap bitmap = ImageManager.getInstance().getImage(Uri.parse(this.movies.get(position).getImageUri())).getBitmap();
        return bitmap;
    }
}