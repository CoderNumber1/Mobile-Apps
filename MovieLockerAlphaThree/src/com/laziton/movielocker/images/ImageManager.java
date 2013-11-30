package com.laziton.movielocker.images;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.MovieLockerApp;
import com.laziton.movielocker.data.Image;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;

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
	LruCache<Integer,byte[]> memCache;
	Lock lock;
	
	private final static ImageManager _instance = new ImageManager();
	public static ImageManager getInstance() { return _instance; }
	private ImageManager(){
		int maxMem = (int)(Runtime.getRuntime().freeMemory() / 1024);
		int cacheSize = maxMem / 8; //Cache is 1/8 total memory.
		
		this.memCache = new LruCache<Integer, byte[]>(cacheSize);
		this.lock = new ReentrantLock();
	}
	
	public Uri generateTempUri(){
		String imageName = "movie_locker_temp_image" + String.valueOf(UUID.randomUUID()) + ".jpg";
		File imageFile = new File(MovieLockerApp.context.getExternalCacheDir(), imageName);
		imageFile.delete();
		try {
			FileOutputStream fileOut = MovieLockerApp.context.openFileOutput(imageName, Context.MODE_WORLD_WRITEABLE);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		imageFile = new File(MovieLockerApp.context.getExternalCacheDir(), imageName);
		
		return Uri.fromFile(imageFile);
	}
	
	public BitmapDrawable getImage(int movieId){
		BitmapDrawable result = null;
		byte[] resultBytes = this.memCache.get(movieId);
		if(resultBytes == null){
			this.lock.lock();
			try{
				resultBytes = this.loadImageBytes(movieId);
				if(resultBytes != null){
					this.memCache.put(movieId, resultBytes);
				}
			}
			finally{
				this.lock.unlock();
			}
		}
		
		if(resultBytes != null){
			result = this.loadImage(resultBytes);
		}
		
		return result;
	}
	
	public void getImageAsync(int movieId, GetImageAsyncCallback callback){
		new ImageLoaderTask(callback).execute(movieId);
	}
	
	public void getImageAsync(Uri uri, GetImageAsyncCallback callback){
		new ImageFileLoaderTask(callback).execute(uri);
	}
	
	public void setImageAsync(int movieId, Uri uri, SetImageAsyncCallback callback){
		ImageSetArgs args = new ImageSetArgs();
		args.movieId = movieId;
		args.uri = uri;
		
		new ImageSetTask(callback).execute(args);
	}

	public void cleanupTempImages(){
		for(String file : MovieLockerApp.context.getFilesDir().list()){
			if(file.startsWith("movie_locker_temp_image")){
				File f = new File(MovieLockerApp.context.getExternalCacheDir(), file);
				f.delete();
			}		
		}
	}
	
	@SuppressWarnings("deprecation")
	public byte[] loadImageBytes(Uri uri){
		byte[] imageData = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			InputStream imageStream = MovieLockerApp.context.getContentResolver().openInputStream(uri);
			
			int b;  
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			while ((b = imageStream.read(buffer)) != -1) {  
				bo.write(buffer, 0, b);  
			}  
			
			imageData = bo.toByteArray();  
			bo.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageData;
	}
	public BitmapDrawable loadImage(Uri uri){
		return loadImage(loadImageBytes(uri));
	}

	public byte[] loadImageBytes(int movieId){
		byte[] iData = null;
		IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
    	dataService.Open();
    	Image i = dataService.GetImageByMovieId(movieId);  
    	if(i != null){
    		iData = i.getImageData();
    	}
		dataService.Close();
		return iData;
	}
	public BitmapDrawable loadImage(int movieId){
		return this.loadImage(loadImageBytes(movieId));
	}

	public BitmapDrawable loadImage(byte[] imageBytes){
		if(imageBytes == null)
			return null;
		
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
		BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
		
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
		
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
		
		if(bitmap == null)
			return null;
		else
			return new BitmapDrawable(MovieLockerApp.context.getResources(), bitmap);
	}
	
	public void cleanImageView(ImageView view){
		if(!(view.getDrawable() instanceof BitmapDrawable)){
			return;
		}
		
		BitmapDrawable b = (BitmapDrawable)view.getDrawable();
		b.getBitmap().recycle();
		view.setImageDrawable(null);
	}
	
	public interface GetImageAsyncCallback{
		public void callback(BitmapDrawable image);
	}
	
	public interface SetImageAsyncCallback{
		public void callback(Boolean image);
	}
	
	private class ImageLoaderTask extends AsyncTask<Integer,Integer,BitmapDrawable>{
		GetImageAsyncCallback callback;
		
		public ImageLoaderTask(GetImageAsyncCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected BitmapDrawable doInBackground(Integer... movieId) {
			return getImage(movieId[0]);
		}

		@Override
		protected void onPostExecute(BitmapDrawable result) {
			this.callback.callback(result);
		}
		
	}
	
	private class ImageFileLoaderTask extends AsyncTask<Uri,Integer,BitmapDrawable>{
		GetImageAsyncCallback callback;
		
		public ImageFileLoaderTask(GetImageAsyncCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected BitmapDrawable doInBackground(Uri... uri) {
			return loadImage(uri[0]);
		}

		@Override
		protected void onPostExecute(BitmapDrawable result) {
			this.callback.callback(result);
		}
	}
	
	private class ImageSetArgs{
		public Integer movieId;
		public Uri uri;
	}
	
	private class ImageSetTask extends AsyncTask<ImageSetArgs,Integer,Boolean>{
		SetImageAsyncCallback callback;
		
		public ImageSetTask(SetImageAsyncCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected Boolean doInBackground(ImageSetArgs... args) {
			byte[] imageBytes = loadImageBytes(args[0].uri);
			
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	Image image = args[0].movieId > 0 ? dataService.GetImageByMovieId(args[0].movieId) : null;  
	    	if(image == null){
	    		image = new Image();
	    		image.setMovieId(args[0].movieId);
	    		image.setImageData(imageBytes);
	    		dataService.InsertImage(image);
	    	}
	    	else{
	    		image.setImageData(imageBytes);
	    		dataService.UpdateImage(image);
	    	}
			dataService.Close();
			
			if(ImageManager.this.memCache.get(args[0].movieId) != null)
				ImageManager.this.memCache.remove(args[0].movieId);
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			this.callback.callback(result);
		}
		
	}
}
