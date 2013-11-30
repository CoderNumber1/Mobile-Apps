package com.laziton.movielocker.dataservices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	public void Open(){
		this.database = this.sqlContext.getWritableDatabase();
	}
	
	@Override
	public void Close(){
		this.database.close();
	}
	
	@Override
	public boolean IsOpen(){
		return this.database != null && this.database.isOpen();
	}
	
	@Override
	public Movie GetMovie(int id){
		Movie movie = null;
		try {
			movie = this.sqlContext.getMovieDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movie;
	}

	@Override
	public ArrayList<Movie> GetMovies() {
		ArrayList<Movie> result = null;
		
		try {
			Dao<Movie,Integer> d = this.sqlContext.getMovieDao();
			result = new ArrayList<Movie>();
			result.addAll(d.queryBuilder().orderBy(MovieLockerSqlContext.MOVIE_NAME, true).query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int InsertMovie(Movie movie) {
		int result = 0;
		try {
			this.sqlContext.getMovieDao().create(movie);
			result = movie.getId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void UpdateMovie(Movie movie) {
		try {
			this.sqlContext.getMovieDao().update(movie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteMovie(Movie movie) {
		try {
			Image i = this.GetImageByMovieId(movie.getId());
			if(i != null)
				this.DeleteImage(i);
			
			this.sqlContext.getMovieDao().delete(movie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Genre GetGenre(int id) {
		Genre result = null;
		
		try {
			result =  this.sqlContext.getGenreDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public ArrayList<Genre> GetGenres() {
		ArrayList<Genre> result = new ArrayList<Genre>();
		
		try {
			result.addAll(this.sqlContext.getGenreDao().queryBuilder().orderBy(MovieLockerSqlContext.GENRE_NAME, true).query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void InsertGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().create(genre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().update(genre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void DeleteGenre(Genre genre) {
		try {
			this.sqlContext.getGenreDao().delete(genre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Override
	public ArrayList<Movie> GetMoviesByCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(CollectionMovie cm : collectionMovies){
			ids.add(cm.getMovieId());
		}
		return this.GetMovies(ids);
	}

	@Override
	public ArrayList<Movie> GetMovies(ArrayList<Integer> movieIds) {
		ArrayList<Movie> result = null;
		Dao<Movie,Integer> dao = this.sqlContext.getMovieDao();
		QueryBuilder<Movie,Integer> builder = dao.queryBuilder();
		try {
			builder.where().in(MovieLockerSqlContext.MOVIE_ID, movieIds);
			builder.orderBy(MovieLockerSqlContext.MOVIE_NAME, true);
			result = new ArrayList<Movie>(builder.query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Image GetImage(int id) {
		Image result = null;
		try {
			result = this.sqlContext.getImageDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Image GetImageByMovieId(int movieId) {
		Image result = null;
		Dao<Image,Integer> dao = this.sqlContext.getImageDao();
		QueryBuilder<Image, Integer> builder = dao.queryBuilder();
		try {
			builder.where().eq(MovieLockerSqlContext.IMAGE_MOVIE_ID, movieId);
			List<Image> resultSet = builder.query();
			if(resultSet.size() > 0)
				result = resultSet.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<Image> GetImages() {
		ArrayList<Image> result = null;
		try {
			result = new ArrayList<Image>(this.sqlContext.getImageDao().queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void InsertImage(Image image) {
		try {
			this.sqlContext.getImageDao().create(image);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateImage(Image image) {
		try {
			this.sqlContext.getImageDao().update(image);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteImage(Image image) {
		try {
			this.sqlContext.getImageDao().delete(image);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Collection GetCollection(int id) {
		Collection result = null;
		try {
			result = this.sqlContext.getCollectionDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<Collection> GetCollections() {
		ArrayList<Collection> result = null;
		try {
			result = new ArrayList<Collection>(this.sqlContext.getCollectionDao().queryBuilder().orderBy(MovieLockerSqlContext.COLLECTION_NAME, true).query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public ArrayList<Collection> GetCollectionsByCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(CollectionMovie cm : collectionMovies){
			ids.add(cm.getCollectionId());
		}
		return this.GetCollections(ids);
	}

	@Override
	public ArrayList<Collection> GetCollections(ArrayList<Integer> collectionIds) {
		ArrayList<Collection> result = null;
		QueryBuilder<Collection,Integer> builder = this.sqlContext.getCollectionDao().queryBuilder();
		try {
			builder.where().in(MovieLockerSqlContext.COLLECTION_ID, collectionIds);
			builder.orderBy(MovieLockerSqlContext.COLLECTION_NAME, true);
			result = new ArrayList<Collection>(builder.query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void InsertCollection(Collection collection) {
		try {
			this.sqlContext.getCollectionDao().create(collection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateCollection(Collection collection) {
		try {
			this.sqlContext.getCollectionDao().update(collection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteCollection(Collection collection) {
		try {
			this.sqlContext.getCollectionDao().delete(collection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public CollectionMovie GetCollectionMovie(int id) {
		CollectionMovie result = null;
		try {
			result = this.sqlContext.getCollectionMovieDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<CollectionMovie> GetCollectionMovies(Integer collectionId, Integer movieId) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<CollectionMovie> GetCollectionMovies(ArrayList<Integer> collectionIds, ArrayList<Integer> movieIds){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void InsertCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().create(collectionMovie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().update(collectionMovie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteCollectionMovie(CollectionMovie collectionMovie) {
		try {
			this.sqlContext.getCollectionMovieDao().delete(collectionMovie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteCollectionMovies(ArrayList<CollectionMovie> collectionMovies) {
		try {
			this.sqlContext.getCollectionMovieDao().delete(collectionMovies);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Movie> GetMoviesByFilter(MovieFilter filter) {
		ArrayList<Movie> result = null;
		Dao<Movie,Integer> dao = this.sqlContext.getMovieDao();
		QueryBuilder<Movie,Integer> query = dao.queryBuilder();
		if(filter == null)
			try {
				result = new ArrayList<Movie>(query.orderBy(MovieLockerSqlContext.MOVIE_NAME, true).query());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else{
			try {
				boolean needAnd = false;
				Where where = query.where();
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
						catch(Exception ex){}
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
					
					for(CollectionMovie collectionMovie : this.GetCollectionMovies(collectionIds, null)){
						collectionMovieIds.add(collectionMovie.getMovieId());
					}
					
					where = where.in(MovieLockerSqlContext.MOVIE_ID, collectionMovieIds);
					if(needAnd)
						where = where.and(2);
					
					needAnd = true;
				}
				
				if(filter.isOwned() || filter.isWishList()){
					
					if(filter.isOwned())
						where = where.eq(MovieLockerSqlContext.MOVIE_OWNED, true);
					if(filter.isWishList())
						where = where.eq(MovieLockerSqlContext.MOVIE_OWNED, false);
					
					if(filter.isOwned() && filter.isWishList())
						where = where.or(2);
					
					if(needAnd)
						where = where.and(2);
				}

				query.orderBy(MovieLockerSqlContext.MOVIE_NAME, true);
				result = new ArrayList<Movie>(query.query());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public MovieFilter GetMovieFilter(int id) {
		MovieFilter result = null;
		
		try {
			result = this.sqlContext.getMovieFilterDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public MovieFilter GetMovieFilter(String filterName) {
		MovieFilter result = null;
		Dao<MovieFilter,Integer> dao = this.sqlContext.getMovieFilterDao();
		QueryBuilder<MovieFilter,Integer> query = dao.queryBuilder();
		
		try {
			query.where().eq(MovieLockerSqlContext.MOVIEFILTER_FILTER_NAME, filterName);
			result = query.queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public ArrayList<MovieFilter> GetMovieFilters() {
		ArrayList<MovieFilter> result = null;
		
		try {
			result = new ArrayList<MovieFilter>(this.sqlContext.getMovieFilterDao().queryForAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void InsertMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().create(filter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void UpdateMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().update(filter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void DeleteMovieFilter(MovieFilter filter) {
		try {
			this.sqlContext.getMovieFilterDao().delete(filter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
