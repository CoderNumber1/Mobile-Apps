package com.laziton.mlalphatwo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity activity, String path){
		Display display = activity.getWindowManager().getDefaultDisplay();
		
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
		BitmapFactory.decodeFile(path, options);
		
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
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return new BitmapDrawable(activity.getResources(), bitmap);
	}
	
	public static void cleanImageView(ImageView view){
		if(!(view.getDrawable() instanceof BitmapDrawable)){
			return;
		}
		
		BitmapDrawable b = (BitmapDrawable)view.getDrawable();
		b.getBitmap().recycle();
		view.setImageDrawable(null);
	}
}
