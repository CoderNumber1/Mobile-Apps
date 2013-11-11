package com.laziton.movielocker.dataservices;

import com.laziton.movielocker.MovieLockerApp;

public class DataServiceFactory {
	private static DataServiceFactory instance;
	
	private DataServiceFactory(){}
	
	public static DataServiceFactory GetInstance(){
		if(instance == null)
			instance = new DataServiceFactory();
		
		return instance;
	}
	
	public IDataService GetDataService(){
		IDataService result;
		
		result = new SqlLiteDataService(MovieLockerApp.context);
		
		return result;
	}
}
