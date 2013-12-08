package com.laziton.movielocker.dataservices;

import java.util.ArrayList;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;

public interface IFilteredMovieDataService {
	public void open();
	public void close();
	public boolean isOpen();
	
	public MovieFilter getMovieFilter();
	public void setMovieFilter(MovieFilter filter);
	
	public ArrayList<Movie> getFilteredMovies();
}
