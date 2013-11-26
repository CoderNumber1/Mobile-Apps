package com.laziton.movielocker.dataservices;

import java.util.ArrayList;

import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Image;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;

import android.database.Cursor;

public interface IDataService {
	public void Open();
	public void Close();
	
	public Movie GetMovie(int id);
	public ArrayList<Movie> GetMovies();
	public ArrayList<Movie> GetMoviesByFilter(MovieFilter filter);
	public ArrayList<Movie> GetMoviesByCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	public ArrayList<Movie> GetMovies(ArrayList<Integer> movieIds);
	
	public int InsertMovie(Movie movie);
	public void UpdateMovie(Movie movie);
	public void DeleteMovie(Movie movie);
	
	public Genre GetGenre(int id);
	public ArrayList<Genre> GetGenres();
	
	public void InsertGenre(Genre genre);
	public void UpdateGenre(Genre genre);
	public void DeleteGenre(Genre genre);
	
	public Image GetImage(int id);
	public Image GetImageByMovieId(int movieId);
	public ArrayList<Image> GetImages();
	
	public void InsertImage(Image image);
	public void UpdateImage(Image image);
	public void DeleteImage(Image image);
	
	public Collection GetCollection(int id);
	public ArrayList<Collection> GetCollections();
	public ArrayList<Collection> GetCollectionsByCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	public ArrayList<Collection> GetCollections(ArrayList<Integer> collectionIds);
	
	public void InsertCollection(Collection collection);
	public void UpdateCollection(Collection collection);
	public void DeleteCollection(Collection collection);
	
	public CollectionMovie GetCollectionMovie(int id);
	public ArrayList<CollectionMovie> GetCollectionMovies(Integer collectionId, Integer movieId);
	public ArrayList<CollectionMovie> GetCollectionMovies(ArrayList<Integer> collectionIds, ArrayList<Integer> movieIds);
	
	public void InsertCollectionMovie(CollectionMovie collectionMovie);
	public void UpdateCollectionMovie(CollectionMovie collectionMovie);
	public void DeleteCollectionMovie(CollectionMovie collectionMovie);
	public void DeleteCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	
	public MovieFilter GetMovieFilter(int id);
	public MovieFilter GetMovieFilter(String filterName);
	public ArrayList<MovieFilter> GetMovieFilters();
	
	public void InsertMovieFilter(MovieFilter filter);
	public void UpdateMovieFilter(MovieFilter filter);
	public void DeleteMovieFilter(MovieFilter filter);
}
