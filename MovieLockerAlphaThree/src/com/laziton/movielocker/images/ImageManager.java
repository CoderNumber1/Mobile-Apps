package com.laziton.movielocker.images;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.laziton.movielocker.MovieLockerApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageManager {
	LruCache<String,BitmapDrawable> memCache;
	
	private final static ImageManager _instance = new ImageManager();
	public static ImageManager getInstance() { return _instance; }
	private ImageManager(){
		int maxMem = (int)(Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMem / 8; //Cache is 1/8 total memory.
		
		this.memCache = new LruCache<String, BitmapDrawable>(cacheSize);
	}
	
	public BitmapDrawable getImage(Uri uri){
		BitmapDrawable result = null;
		result = this.memCache.get(uri.toString());
		if(result == null){
			result = this.loadImage(uri);
			this.memCache.put(uri.toString(), result);
		}
		
		return result;
	}
	
	public void getImageAsync(Uri uri, GetImageAsyncCallback callback){
		new ImageLoaderTask(callback).execute(uri);
	}
	
	@SuppressWarnings("deprecation")
	public BitmapDrawable loadImage(Uri uri){
		WindowManager windowManager = (WindowManager)MovieLockerApp.context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2){
			Point size = new Point();
			display.getSize(size);
			destWidth = size.x;
			destHeight = size.y;
		}
		else{
			destWidth = display.getWidth();
			destHeight = display.getHeight();
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			InputStream imageStream = MovieLockerApp.context.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(imageStream, null, options);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth){
			if(srcWidth > srcHeight){
				inSampleSize = Math.round(srcHeight / destHeight);
			}
			else{
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = null;
		try {
			InputStream imageStream = MovieLockerApp.context.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(imageStream, null, options);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		
		if(bitmap == null)
			return null;
		else
			return new BitmapDrawable(MovieLockerApp.context.getResources(), bitmap);
	}

	public void cleanImageView(ImageView view){
//		if(!(view.getDrawable() instanceof BitmapDrawable)){
//			return;
//		}
//		
//		BitmapDrawable b = (BitmapDrawable)view.getDrawable();
//		b.getBitmap().recycle();
		view.setImageDrawable(null);
	}
	
	public interface GetImageAsyncCallback{
		public void callback(BitmapDrawable image);
	}
	
	private class ImageLoaderTask extends AsyncTask<Uri,Integer,BitmapDrawable>{
		GetImageAsyncCallback callback;
		
		public ImageLoaderTask(GetImageAsyncCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected BitmapDrawable doInBackground(Uri... uri) {
			return getImage(uri[0]);
		}

		@Override
		protected void onPostExecute(BitmapDrawable result) {
			this.callback.callback(result);
		}
		
	}
}
