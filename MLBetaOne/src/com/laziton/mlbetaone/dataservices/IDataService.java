package com.laziton.mlbetaone.dataservices;

import java.util.ArrayList;

import com.laziton.mlbetaone.data.Movie;

import android.database.Cursor;

public interface IDataService {
	public void Open();
	public void Close();
	
	public Movie GetMovie(int id);
	public Cursor GetMovieCursor();
	public ArrayList<Movie> GetMovies();
	
	public void InsertMovie(Movie movie);
	public void UpdateMovie(Movie movie);
	public void DeleteMovie(Movie movie);
}
