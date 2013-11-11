package com.laziton.movielocker.coverflow;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class BaseCoverFlowImageAdapter extends BaseAdapter {

    private static final String TAG = BaseCoverFlowImageAdapter.class.getSimpleName();
    private float width = 125;
    private float height = 200;

    public BaseCoverFlowImageAdapter() {
        super();
    }

    public synchronized void setWidth(final float width) {
        this.width = width;
    }

    public synchronized void setHeight(final float height) {
        this.height = height;
    }

    @Override
    public final Bitmap getItem(final int position) {
        final Bitmap bitmap = createBitmap(position);
        return bitmap;
    }

    protected abstract Bitmap createBitmap(int position);

    @Override
    public final synchronized long getItemId(final int position) {
        return position;
    }

    @Override
    public final synchronized ImageView getView(final int position, final View convertView, final ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            final Context context = parent.getContext();
            Log.v(TAG, "Creating Image view at position: " + position + ":" + this);
            imageView = new ImageView(context);
            imageView.setLayoutParams(new CoverFlow.LayoutParams((int) width, (int) height));
        } else {
            Log.v(TAG, "Reusing view at position: " + position + ":" + this);
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(getItem(position));
        return imageView;
    }

}