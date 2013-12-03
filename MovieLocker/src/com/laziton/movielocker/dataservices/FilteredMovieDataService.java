package com.laziton.movielocker.dataservices;

import java.util.ArrayList;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;

public class FilteredMovieDataService implements IFilteredMovieDataService {
	public static String DEFAULT_FILTER_NAME = "Default";
	
	private IDataService dataService;
	private MovieFilter filter = null;
	
	public FilteredMovieDataService(IDataService dataService){
		this.dataService = dataService;
		this.Open();
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.Close();
		
		super.finalize();
	}

	@Override
	public void Open() {
		if(!this.dataService.IsOpen()){
			this.dataService.Open();
		}
	}
	@Override
	public void Close() {
		if(this.dataService.IsOpen()){
			this.dataService.Close();
		}
	}
	@Override
	public boolean IsOpen() {
		return this.dataService.IsOpen();
	}

	@Override
	public MovieFilter GetMovieFilter() {
		if(this.filter == null){
			this.filter = this.dataService.GetMovieFilter(FilteredMovieDataService.DEFAULT_FILTER_NAME);
			
			if(this.filter == null){
				this.filter = new MovieFilter();
				this.filter.setFilterName(FilteredMovieDataService.DEFAULT_FILTER_NAME);
				this.filter.setOwned(true);
				this.filter.setWishList(true);
				this.dataService.InsertMovieFilter(this.filter);
			}
		}
		
		return this.filter;
	}

	@Override
	public void SetMovieFilter(MovieFilter filter) {
		this.filter = filter;
		
		if(this.filter.getId() > 0)
			this.dataService.UpdateMovieFilter(this.filter);
		else
			this.dataService.InsertMovieFilter(this.filter);
	}

	@Override
	public ArrayList<Movie> GetFilteredMovies() {
		ArrayList<Movie> result = null;
		
		result = this.dataService.GetMoviesByFilter(this.GetMovieFilter());
		
		return result;
	}
}
