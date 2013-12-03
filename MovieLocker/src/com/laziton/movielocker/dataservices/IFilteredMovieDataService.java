package com.laziton.movielocker.dataservices;

import java.util.ArrayList;

import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;

public interface IFilteredMovieDataService {
	public void Open();
	public void Close();
	public boolean IsOpen();
	
	public MovieFilter GetMovieFilter();
	public void SetMovieFilter(MovieFilter filter);
	
	public ArrayList<Movie> GetFilteredMovies();
}
