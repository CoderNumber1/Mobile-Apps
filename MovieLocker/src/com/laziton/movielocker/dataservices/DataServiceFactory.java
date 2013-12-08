package com.laziton.movielocker.dataservices;

import com.laziton.movielocker.MovieLockerApp;

public class DataServiceFactory {
	private static DataServiceFactory instance;
	
	private DataServiceFactory(){}
	
	public static DataServiceFactory getInstance(){
		if(instance == null)
			instance = new DataServiceFactory();
		
		return instance;
	}
	
	public IDataService getDataService(){
		IDataService result;
		
		result = new SqlLiteDataService(MovieLockerApp.context);
		
		return result;
	}
	
	public IFilteredMovieDataService getFilteredMovieDataService(){
		IDataService dataService = this.getDataService();
		IFilteredMovieDataService result = new FilteredMovieDataService(dataService);
		
		return result;
	}
}
