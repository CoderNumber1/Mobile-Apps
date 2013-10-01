package edu.uco.kjames21.p05;

import java.util.Comparator;

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
			return arg0.getFirstName().compareTo(arg1.getFirstName());
		}
		else if(this.mode == COMPARE_MODE_AGE){
			return arg0.getAge().compareTo(arg1.getAge());
		}
		else if(this.mode == COMPARE_MODE_LAST_NAME){
			return arg0.getLastName().compareTo(arg1.getLastName()); 
		}
		else{
			return arg0.getLastName().compareTo(arg1.getLastName());
		}
	}
}
