package com.laziton.movielocker.dataservices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Image;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.data.MovieFilter;
import com.laziton.movielocker.data.MovieLockerSqlContext;

public class SqlLiteDataService implements IDataService {
	private MovieLockerSqlContext sqlContext;
	private SQLiteDatabase database;
	
	protected SqlLiteDataService(Context context){
		this.sqlContext = new MovieLockerSqlContext(context);
	}
	
	@Override
	public void open(){
		this.database = this.sqlContext.getWritableDatabase();
	}
	
	@Override
	public void close(){
		this.database.close();
	}
	
	@Override
	public boolean isOpen(){
		return this.database != null && this.database.isOpen();
	}
	
	@Override
	public Movie getMovie(int id){
		Movie movie = null;
		try {
			movie = this.sqlContext.getMovieDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getMovie", e.toString());
		}
		
		return movie;
	}

	@Override
	public ArrayList<Movie> getMovies() {
		ArrayList<Movie> result = null;
		
		try {
			Dao<Movie,Integer> d = this.sqlContext.getMovieDao();
			result = new ArrayList<Movie>();
			result.addAll(d.queryBuilder().orderBy(MovieLockerSqlContext.MOVIE_NAME, true).query());
		} catch (SQLException e) {
			Log.e("getMovies", e.toString());
		}
		
		return result;
	}

	@Override
	public int insertMovie(Movie movie) {
		int result = 0;
		try {
			this.sqlContext.getMovieDao().create(movie);
			result = movie.getId();
		} catch (SQLException e) {
			Log.e("insertMovie", e.toString());
		}
		return result;
	}

	@Override
	public void updateMovie(Movie movie) {
		try {
			this.sqlContext.getMovieDao().update(movie);
		} catch (SQLException e) {
			Log.e("updateMovie", e.toString());
		}
	}

	@Override
	public void deleteMovie(Movie movie) {
		try {
			Image i = this.getImageByMovieId(movie.getId());
			if(i != null)
				this.deleteImage(i);
			
			this.sqlContext.getMovieDao().delete(movie);
		} catch (SQLException e) {
			Log.e("deleteMovie", e.toString());
		}
	}

	@Override
	public Genre getGenre(int id) {
		Genre result = null;
		
		try {
			result =  this.sqlContext.getGenreDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getGenre", e.toString());
		}
		
		return result;
	}

	@Override
	public ArrayList<Genre> getGenres() {
		ArrayList<Genre> result = new ArrayList<Genre>();
		
		try {
			result.addAll(this.sqlContext.getGenreDao().queryBuilder().orderBy(MovieLockerSqlContext.GENRE_NAME, true).query());
		} catch (SQLException e) {
			Log.e("getGenres", e.toString());
		}
		
		return result;
	}

	@Override
	public void insertGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().create(genre);
		} catch (SQLException e) {
			Log.e("insertGenre", e.toString());
		}
	}

	@Override
	public void updateGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().update(genre);
		} catch (SQLException e) {
			Log.e("updateGenre", e.toString());
		}	
	}

	@Override
	public void deleteGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().delete(genre);
		} catch (SQLException e) {
			Log.e("deleteGenre", e.toString());
		}	
	}
	
	@Override
	public ArrayList<Movie> getMoviesByCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(CollectionMovie cm : collectionMovies){
			ids.add(cm.getMovieId());
		}
		return this.getMovies(ids);
	}

	@Override
	public ArrayList<Movie> getMovies(ArrayList<Integer> movieIds) {
		ArrayList<Movie> result = null;
		Dao<Movie,Integer> dao = this.sqlContext.getMovieDao();
		QueryBuilder<Movie,Integer> builder = dao.queryBuilder();
		try {
			builder.where().in(MovieLockerSqlContext.MOVIE_ID, movieIds);
			builder.orderBy(MovieLockerSqlContext.MOVIE_NAME, true);
			result = new ArrayList<Movie>(builder.query());
		} catch (SQLException e) {
			Log.e("getMovies", e.toString());
		}
		return result;
	}

	@Override
	public Image getImage(int id) {
		Image result = null;
		try {
			result = this.sqlContext.getImageDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getImage", e.toString());
		}
		return result;
	}

	@Override
	public Image getImageByMovieId(int movieId) {
		Image result = null;
		Dao<Image,Integer> dao = this.sqlContext.getImageDao();
		QueryBuilder<Image, Integer> builder = dao.queryBuilder();
		try {
			builder.where().eq(MovieLockerSqlContext.IMAGE_MOVIE_ID, movieId);
			List<Image> resultSet = builder.query();
			if(resultSet.size() > 0)
				result = resultSet.get(0);
		} catch (SQLException e) {
			Log.e("getImageByMovieId", e.toString());
		}
		return result;
	}

	@Override
	public ArrayList<Image> getImages() {
		ArrayList<Image> result = null;
		try {
			result = new ArrayList<Image>(this.sqlContext.getImageDao().queryForAll());
		} catch (SQLException e) {
			Log.e("getImages", e.toString());
		}
		return result;
	}

	@Override
	public void insertImage(Image image) {
		try {
			this.sqlContext.getImageDao().create(image);
		} catch (SQLException e) {
			Log.e("insertImage", e.toString());
		}
	}

	@Override
	public void updateImage(Image image) {
		try {
			this.sqlContext.getImageDao().update(image);
		} catch (SQLException e) {
			Log.e("updateImage", e.toString());
		}
	}

	@Override
	public void deleteImage(Image image) {
		try {
			this.sqlContext.getImageDao().delete(image);
		} catch (SQLException e) {
			Log.e("deleteImage", e.toString());
		}
	}

	@Override
	public Collection getCollection(int id) {
		Collection result = null;
		try {
			result = this.sqlContext.getCollectionDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getCollection", e.toString());
		}
		return result;
	}

	@Override
	public ArrayList<Collection> getCollections() {
		ArrayList<Collection> result = null;
		try {
			result = new ArrayList<Collection>(this.sqlContext.getCollectionDao().queryBuilder().orderBy(MovieLockerSqlContext.COLLECTION_NAME, true).query());
		} catch (SQLException e) {
			Log.e("getCollections", e.toString());
		}
		return result;
	}
	
	@Override
	public ArrayList<Collection> getCollectionsByCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(CollectionMovie cm : collectionMovies){
			ids.add(cm.getCollectionId());
		}
		return this.getCollections(ids);
	}

	@Override
	public ArrayList<Collection> getCollections(ArrayList<Integer> collectionIds) {
		ArrayList<Collection> result = null;
		QueryBuilder<Collection,Integer> builder = this.sqlContext.getCollectionDao().queryBuilder();
		try {
			builder.where().in(MovieLockerSqlContext.COLLECTION_ID, collectionIds);
			builder.orderBy(MovieLockerSqlContext.COLLECTION_NAME, true);
			result = new ArrayList<Collection>(builder.query());
		} catch (SQLException e) {
			Log.e("getCollections", e.toString());
		}
		return result;
	}

	@Override
	public void insertCollection(Collection collection) {
		try {
			this.sqlContext.getCollectionDao().create(collection);
		} catch (SQLException e) {
			Log.e("insertCollection", e.toString());
		}
	}

	@Override
	public void updateCollection(Collection collection) {
		try {
			this.sqlContext.getCollectionDao().update(collection);
		} catch (SQLException e) {
			Log.e("updateCollection", e.toString());
		}
	}

	@Override
	public void deleteCollection(Collection collection) {
		try {
			this.deleteCollectionMovies(this.getCollectionMovies(collection.getId(), null));
			this.sqlContext.getCollectionDao().delete(collection);
		} catch (SQLException e) {
			Log.e("deleteCollection", e.toString());
		}
	}

	@Override
	public CollectionMovie getCollectionMovie(int id) {
		CollectionMovie result = null;
		try {
			result = this.sqlContext.getCollectionMovieDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getCollectionMovie", e.toString());
		}
		return result;
	}

	@Override
	public ArrayList<CollectionMovie> getCollectionMovies(Integer collectionId, Integer movieId) {
		ArrayList<CollectionMovie> result = null;
		Dao<CollectionMovie,Integer> dao = this.sqlContext.getCollectionMovieDao();
		QueryBuilder<CollectionMovie,Integer> builder = dao.queryBuilder();
		try {
			
			if(collectionId != null)
				builder.where().eq(MovieLockerSqlContext.COLLECTIONMOVIE_COLLECTION_ID, collectionId);
			
			if(movieId != null)
				builder.where().eq(MovieLockerSqlContext.COLLECTIONMOVIE_MOVIE_ID, movieId);
			
			if(collectionId != null && movieId != null)
				builder.where().and(2);
			
			result = new ArrayList<CollectionMovie>(builder.query());
		} catch (SQLException e) {
			Log.e("getCollectionMovies", e.toString());
		}
		return result;
	}
	
	public ArrayList<CollectionMovie> getCollectionMovies(ArrayList<Integer> collectionIds, ArrayList<Integer> movieIds){
		ArrayList<CollectionMovie> result = null;
		Dao<CollectionMovie,Integer> dao = this.sqlContext.getCollectionMovieDao();
		QueryBuilder<CollectionMovie,Integer> builder = dao.queryBuilder();
		try {
			
			if(collectionIds != null)
				builder.where().in(MovieLockerSqlContext.COLLECTIONMOVIE_COLLECTION_ID, collectionIds);
			
			if(movieIds != null)
				builder.where().in(MovieLockerSqlContext.COLLECTIONMOVIE_MOVIE_ID, movieIds);
			
			if(collectionIds != null && movieIds != null)
				builder.where().and(2);
			
			result = new ArrayList<CollectionMovie>(builder.query());
		} catch (SQLException e) {
			Log.e("getCollectionMovies", e.toString());
		}
		return result;
	}

	@Override
	public void insertCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().create(collectionMovie);
		} catch (SQLException e) {
			Log.e("insertCollectionMovie", e.toString());
		}
	}

	@Override
	public void updateCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().update(collectionMovie);
		} catch (SQLException e) {
			Log.e("updateCollectionMovie", e.toString());
		}
	}

	@Override
	public void deleteCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().delete(collectionMovie);
		} catch (SQLException e) {
			Log.e("deleteCollectionMovie", e.toString());
		}
	}

	@Override
	public void deleteCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		try {
			this.sqlContext.getCollectionMovieDao().delete(collectionMovies);
		} catch (SQLException e) {
			Log.e("deleteCollectionMovies", e.toString());
		}
	}

	@Override
	public ArrayList<Movie> getMoviesByFilter(MovieFilter filter) {
		ArrayList<Movie> result = null;
		Dao<Movie,Integer> dao = this.sqlContext.getMovieDao();
		QueryBuilder<Movie,Integer> query = dao.queryBuilder();
		if(filter == null || filter.isCleared())
			try {
				result = new ArrayList<Movie>(query.orderBy(MovieLockerSqlContext.MOVIE_NAME, true).query());
			} catch (SQLException e) {
				Log.e("getMoviesByFilter", e.toString());
			}
		else{
			try {
				boolean needAnd = false;
				Where<Movie,Integer> where = query.where();
				if(filter.getMovieName() != null && !filter.getMovieName().equals("")){
					where = where.like(MovieLockerSqlContext.MOVIE_NAME, '%' + filter.getMovieName().trim().replace(' ', '%') + '%');
					needAnd = true;
				}
				
				if(filter.getGenreIds() != null && !filter.getGenreIds().equals("")){
					
					ArrayList<Integer> genreIds = new ArrayList<Integer>();
					for(String genre : filter.getGenreIds().split(",")){
						try{
							genreIds.add(Integer.parseInt(genre));
						}
						catch(Exception e){
							Log.e("getMoviesByFilter", e.toString());
						}
					}
					where = where.in(MovieLockerSqlContext.MOVIE_GENRE_ID, genreIds);
					if(needAnd)
						where = where.and(2);
					
					needAnd = true;
				}
				
				if(filter.getCollectionIds() != null && !filter.getCollectionIds().equals("")){
					
					ArrayList<Integer> collectionIds = new ArrayList<Integer>();
					HashSet<Integer> collectionMovieIds = new HashSet<Integer>();
					for(String collection : filter.getCollectionIds().split(",")){
						try{
							collectionIds.add(Integer.parseInt(collection));
						}catch(Exception ex){}
					}
					
					for(CollectionMovie collectionMovie : this.getCollectionMovies(collectionIds, null)){
						collectionMovieIds.add(collectionMovie.getMovieId());
					}
					
					where = where.in(MovieLockerSqlContext.MOVIE_ID, collectionMovieIds);
					if(needAnd)
						where = where.and(2);
					
					needAnd = true;
				}
				
				if(filter.isOwned() != filter.isWishList()){
					
					if(filter.isOwned())
						where = where.eq(MovieLockerSqlContext.MOVIE_OWNED, true);
					if(filter.isWishList())
						where = where.eq(MovieLockerSqlContext.MOVIE_OWNED, false);
					
					if(needAnd)
						where = where.and(2);
				}

				query.orderBy(MovieLockerSqlContext.MOVIE_NAME, true);
				result = new ArrayList<Movie>(query.query());
			} catch (SQLException e) {
				Log.e("getMoviesByFilter", e.toString());
			}
		}
		
		return result;
	}

	@Override
	public MovieFilter getMovieFilter(int id) {
		MovieFilter result = null;
		
		try {
			result = this.sqlContext.getMovieFilterDao().queryForId(id);
		} catch (SQLException e) {
			Log.e("getMovieFilter", e.toString());
		}
		
		return result;
	}

	@Override
	public MovieFilter getMovieFilter(String filterName) {
		MovieFilter result = null;
		Dao<MovieFilter,Integer> dao = this.sqlContext.getMovieFilterDao();
		QueryBuilder<MovieFilter,Integer> query = dao.queryBuilder();
		
		try {
			query.where().eq(MovieLockerSqlContext.MOVIEFILTER_FILTER_NAME, filterName);
			result = query.queryForFirst();
		} catch (SQLException e) {
			Log.e("getMovieFilter", e.toString());
		}
		
		return result;
	}

	@Override
	public ArrayList<MovieFilter> getMovieFilters() {
		ArrayList<MovieFilter> result = null;
		
		try {
			result = new ArrayList<MovieFilter>(this.sqlContext.getMovieFilterDao().queryForAll());
		} catch (SQLException e) {
			Log.e("getMovieFilters", e.toString());
		}
		
		return result;
	}

	@Override
	public void insertMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().create(filter);
		} catch (SQLException e) {
			Log.e("insertMovieFilter", e.toString());
		}
	}

	@Override
	public void updateMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().update(filter);
		} catch (SQLException e) {
			Log.e("updateMovieFilter", e.toString());
		}
	}

	@Override
	public void deleteMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().delete(filter);
		} catch (SQLException e) {
			Log.e("deleteMovieFilter", e.toString());
		}
	}
}
