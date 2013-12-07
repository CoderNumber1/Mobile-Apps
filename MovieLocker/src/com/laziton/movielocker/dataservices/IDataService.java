package com.laziton.movielocker.dataservices;

import java.util.ArrayList;

import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Image;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;

public interface IDataService {
	public void open();
	public void close();
	public boolean isOpen();
	
	public Movie getMovie(int id);
	public ArrayList<Movie> getMovies();
	public ArrayList<Movie> getMoviesByFilter(MovieFilter filter);
	public ArrayList<Movie> getMoviesByCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	public ArrayList<Movie> getMovies(ArrayList<Integer> movieIds);
	
	public int insertMovie(Movie movie);
	public void updateMovie(Movie movie);
	public void deleteMovie(Movie movie);
	
	public Genre getGenre(int id);
	public ArrayList<Genre> getGenres();
	
	public void insertGenre(Genre genre);
	public void updateGenre(Genre genre);
	public void deleteGenre(Genre genre);
	
	public Image getImage(int id);
	public Image getImageByMovieId(int movieId);
	public ArrayList<Image> getImages();
	
	public void insertImage(Image image);
	public void updateImage(Image image);
	public void deleteImage(Image image);
	
	public Collection getCollection(int id);
	public ArrayList<Collection> getCollections();
	public ArrayList<Collection> getCollectionsByCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	public ArrayList<Collection> getCollections(ArrayList<Integer> collectionIds);
	
	public void insertCollection(Collection collection);
	public void updateCollection(Collection collection);
	public void deleteCollection(Collection collection);
	
	public CollectionMovie getCollectionMovie(int id);
	public ArrayList<CollectionMovie> getCollectionMovies(Integer collectionId, Integer movieId);
	public ArrayList<CollectionMovie> getCollectionMovies(ArrayList<Integer> collectionIds, ArrayList<Integer> movieIds);
	
	public void insertCollectionMovie(CollectionMovie collectionMovie);
	public void updateCollectionMovie(CollectionMovie collectionMovie);
	public void deleteCollectionMovie(CollectionMovie collectionMovie);
	public void deleteCollectionMovies(ArrayList<CollectionMovie> collectionMovies);
	
	public MovieFilter getMovieFilter(int id);
	public MovieFilter getMovieFilter(String filterName);
	public ArrayList<MovieFilter> getMovieFilters();
	
	public void insertMovieFilter(MovieFilter filter);
	public void updateMovieFilter(MovieFilter filter);
	public void deleteMovieFilter(MovieFilter filter);
}
