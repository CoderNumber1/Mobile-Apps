package com.laziton.movielocker;

import android.os.Bundle;


public interface IDualPaneChannel{
	public void callRightPane(Bundle args);
	public void callLeftPane(Bundle args);
	
	public boolean hasLeftPane();
	public boolean hasRightPane();
}