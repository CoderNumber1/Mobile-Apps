package com.laziton.movielocker.store;

import java.util.ArrayList;
import java.util.HashSet;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IFilteredMovieDataService;

public class FilteredMovieStore {
	public interface IMovieStoreMonitor{
		public void onStoreUpdated();
	}
	
	private ArrayList<Movie> movies = null;
	private HashSet<IMovieStoreMonitor> adapters = null;
	
	private static FilteredMovieStore _instance = null;
	public static FilteredMovieStore getInstance(){
		if(_instance == null)
			_instance = new FilteredMovieStore();
		
		return _instance;
	}
	private FilteredMovieStore(){
		this.movies = new ArrayList<Movie>();
		this.adapters = new HashSet<IMovieStoreMonitor>();
		this.updateStore();
	}
	
	public ArrayList<Movie> getMovies(){
		return this.movies;
	}
	
	public void updateStore(){
		IFilteredMovieDataService filteredService = DataServiceFactory.GetInstance().GetFilteredMovieDataService();
		filteredService.Open();
		this.movies.clear();
		this.movies.addAll(filteredService.GetFilteredMovies());
		filteredService.Close();

		for(IMovieStoreMonitor monitor : this.adapters){
			monitor.onStoreUpdated();
		}
	}
	
	public void registerAdapter(IMovieStoreMonitor monitor){
		this.adapters.add(monitor);
	}
	
	public void unRegisterAdapter(IMovieStoreMonitor monitor){
		if(this.adapters.contains(monitor))
			this.adapters.remove(monitor);
	}
}
