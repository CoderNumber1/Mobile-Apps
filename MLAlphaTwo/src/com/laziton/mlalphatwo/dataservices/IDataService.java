package com.laziton.mlalphatwo.dataservices;

import java.util.ArrayList;

import com.laziton.mlalphatwo.data.Genre;
import com.laziton.mlalphatwo.data.Movie;

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
	
	public Genre GetGenre(int id);
	public ArrayList<Genre> GetGenres();
	
	public void InsertGenre(Genre genre);
	public void UpdateGenre(Genre genre);
	public void DeleteGenre(Genre genre);
}
