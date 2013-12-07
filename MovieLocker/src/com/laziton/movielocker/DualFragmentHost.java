package com.laziton.movielocker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class DualFragmentHost extends FragmentActivity implements IDualPaneChannel {
	protected abstract Fragment createLeftPane();
	protected abstract Fragment createRightPane();
	
	protected int getLayoutResId(){
		return R.layout.activity_dual_pane;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(this.getLayoutResId());
		
		FragmentManager manager = this.getSupportFragmentManager();
		if(this.hasLeftPane()){
			Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
			if(fragment == null){
				fragment = this.createLeftPane();
				manager.beginTransaction()
					.add(R.id.fragmentContainer, fragment)
					.commit();
			}
		}
		
		if(this.hasRightPane()){
			Fragment fragment = manager.findFragmentById(R.id.rightFragmentContainer);
			if(fragment == null){
				fragment = this.createRightPane();
				manager.beginTransaction()
					.add(R.id.rightFragmentContainer, fragment)
					.commit();
			}
		}
	}
	
	@Override
	public void callRightPane(Bundle args) {
		if(this.hasRightPane()){
			FragmentManager manager = this.getSupportFragmentManager();
			Fragment reciever = manager.findFragmentById(R.id.rightFragmentContainer);
			if(reciever instanceof IDualPaneClient){
				((IDualPaneClient)reciever).recieveMessage(args);
			}
		}
	}
	@Override
	public void callLeftPane(Bundle args) {
		if(this.hasLeftPane()){
			FragmentManager manager = this.getSupportFragmentManager();
			Fragment reciever = manager.findFragmentById(R.id.fragmentContainer);
			if(reciever instanceof IDualPaneClient){
				((IDualPaneClient)reciever).recieveMessage(args);
			}
		}
	}
	@Override
	public boolean hasLeftPane() {
		return this.findViewById(R.id.fragmentContainer) != null;
	}
	@Override
	public boolean hasRightPane() {
		return this.findViewById(R.id.rightFragmentContainer) != null;
	}
}
