package com.laziton.movielocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class ScannerActivity extends Activity implements ScanditSDKListener {
	public static final String BARCODE_KEY = "barcode";
	public static final String SYMBOLOGY_KEY = "symbology";

	ScanditSDKAutoAdjustingBarcodePicker picker;
	
	public ScannerActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.picker = new ScanditSDKAutoAdjustingBarcodePicker(this, "GvMvZktPEeOIf1MYVAFRwJefazsnx+GZtDvc5+XO44E", 0);
		this.picker.getOverlayView().addListener(this);
		this.setContentView(this.picker);
	}

	@Override
	protected void onResume() {
	    this.picker.startScanning();
	    super.onResume();
	}

	@Override
	protected void onPause() {
		this.picker.stopScanning();
	    super.onPause();
	}

	@Override
	public void didCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void didManualSearch(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didScanBarcode(String barcode, String symbology) {
		Intent data = new Intent();
		data.putExtra(ScannerActivity.BARCODE_KEY, barcode);
		data.putExtra(ScannerActivity.SYMBOLOGY_KEY, symbology);
		this.setResult(Activity.RESULT_OK, data);
		this.finish();
	}

}
