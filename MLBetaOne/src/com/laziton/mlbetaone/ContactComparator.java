package com.laziton.mlbetaone;

import android.annotation.SuppressLint;
import java.util.Comparator;

@SuppressLint("DefaultLocale")
public class ContactComparator implements Comparator<Contact> {
	public static final int COMPARE_MODE_FIRST_NAME = 1;
	public static final int COMPARE_MODE_LAST_NAME = 3;
	public static final int COMPARE_MODE_AGE = 2;
	
	private int mode;
	
	public ContactComparator(){
		this.mode = COMPARE_MODE_LAST_NAME;
	}
	public ContactComparator(int mode){
		super();
		this.mode = mode;
	}
	
	@Override
	public int compare(Contact arg0, Contact arg1){
		if(this.mode == COMPARE_MODE_FIRST_NAME){
			return arg0.getFirstName().toLowerCase().compareTo(arg1.getFirstName().toLowerCase());
		}
		else if(this.mode == COMPARE_MODE_AGE){
			
			if(arg0.getAge() == null){
				if(arg1.getAge() != null){
					return arg1.getAge().compareTo(arg0.getAge()) * -1;
				}
				else{
					return 0;
				}
			}
			else{
				return arg0.getAge().compareTo(arg1.getAge());
			}
		}
		else if(this.mode == COMPARE_MODE_LAST_NAME){
			return arg0.getLastName().toLowerCase().compareTo(arg1.getLastName().toLowerCase()); 
		}
		else{
			return arg0.getLastName().toLowerCase().compareTo(arg1.getLastName().toLowerCase());
		}
	}
}
