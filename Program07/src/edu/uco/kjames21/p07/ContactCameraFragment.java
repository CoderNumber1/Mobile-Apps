package edu.uco.kjames21.p07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import edu.uco.kjames21.p07.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ContactCameraFragment extends Fragment {
	private static final String TAG = "ContactCameraFragment";
	public static final String EXTRA_PHOTO_FILENAME = "edu.uco.kjames21.p06.ContactCameraFragment.ContactImageFileName";
	
	private Camera camera;
	private SurfaceView view;
	private View frmProgressContainer;
	
	private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			frmProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String fileName = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream os = null;
			boolean success = true;
			
			try{
				os = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
				os.write(data);
			}
			catch(Exception ex){
				Log.e(TAG, "Error writing image to " + fileName + ": ", ex);
				success = false;
			}
			finally{
				try{
					if(os != null){
						os.close();
					}
				}
				catch(Exception ex){
					Log.e(TAG, "Error closing " + fileName + ": ", ex);
					success = false;
				}
			}
			
			if(success){
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, fileName);
				getActivity().setResult(Activity.RESULT_OK, i);
			}
			else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			
			getActivity().finish();
		}
	};
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_contact_camera, container, false);
				
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		frmProgressContainer = v.findViewById(R.id.ProgressContainer);
		frmProgressContainer.setVisibility(View.INVISIBLE);
		
		Button btnTake = (Button)v.findViewById(R.id.btnTakePicture);
		btnTake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(camera != null){
					camera.takePicture(shutterCallback, null, pictureCallback);
				}
			}
		});
		
		this.view = (SurfaceView)v.findViewById(R.id.CameraView);
		SurfaceHolder holder = this.view.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(camera != null){
					camera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try{
					if(camera != null){
						camera.setPreviewDisplay(holder);
					}
				}
				catch(IOException ex){
					Log.e(TAG, "Error setting preview display", ex);
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				if(camera == null) return;
				
				Camera.Parameters parameters = camera.getParameters();
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				camera.setParameters(parameters);
				
				try{
					camera.startPreview();
				}
				catch(Exception ex){
					Log.e(TAG, "Error starting preview:", ex);
					camera.release();
					camera = null;
				}
			}
		});
		
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if(this.camera != null){
			this.camera.release();
			this.camera = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			this.camera = Camera.open(0);
		}
		else{
			this.camera = Camera.open();
		}
	}
	
	/** a simple algorithm to get the largest size available. For a more 
     * robust version, see CameraPreview.java in the ApiDemos 
     * sample app from Android. */
    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }
}
