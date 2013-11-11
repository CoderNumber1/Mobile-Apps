package com.laziton.mlalphatwo.dataservices;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.laziton.mlalphatwo.data.Genre;
import com.laziton.mlalphatwo.data.Movie;
import com.laziton.mlalphatwo.data.MovieLockerSqlContext;

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
	public Cursor GetMovieCursor() {
		Cursor result = this.database.query(MovieLockerSqlContext.MOVIE_TABLE_NAME, null, null, null, null, null, null);
		
		return result;
	}

	@Override
	public ArrayList<Movie> GetMovies() {
		ArrayList<Movie> result = null;
		
		try {
			Dao<Movie,Integer> d = this.sqlContext.getMovieDao();
			result = new ArrayList<Movie>();
			result.addAll(d.queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void InsertMovie(Movie movie) {
		try {
			this.sqlContext.getMovieDao().create(movie);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			result.addAll(this.sqlContext.getGenreDao().queryForAll());
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
}
