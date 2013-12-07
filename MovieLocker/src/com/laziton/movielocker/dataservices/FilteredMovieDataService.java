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
		if(!this.dataService.isOpen()){
			this.dataService.open();
		}
	}
	@Override
	public void Close() {
		if(this.dataService.isOpen()){
			this.dataService.close();
		}
	}
	@Override
	public boolean IsOpen() {
		return this.dataService.isOpen();
	}

	@Override
	public MovieFilter GetMovieFilter() {
		if(this.filter == null){
			this.filter = this.dataService.getMovieFilter(FilteredMovieDataService.DEFAULT_FILTER_NAME);
			
			if(this.filter == null){
				this.filter = new MovieFilter();
				this.filter.setFilterName(FilteredMovieDataService.DEFAULT_FILTER_NAME);
				this.filter.setOwned(true);
				this.filter.setWishList(true);
				this.dataService.insertMovieFilter(this.filter);
			}
		}
		
		return this.filter;
	}

	@Override
	public void SetMovieFilter(MovieFilter filter) {
		this.filter = filter;
		
		if(this.filter.getId() > 0)
			this.dataService.updateMovieFilter(this.filter);
		else
			this.dataService.insertMovieFilter(this.filter);
	}

	@Override
	public ArrayList<Movie> GetFilteredMovies() {
		ArrayList<Movie> result = null;
		
		result = this.dataService.getMoviesByFilter(this.GetMovieFilter());
		
		return result;
	}
}
