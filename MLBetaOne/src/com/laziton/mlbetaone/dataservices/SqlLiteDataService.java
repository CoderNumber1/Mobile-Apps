package com.laziton.mlbetaone.dataservices;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.laziton.mlbetaone.data.Movie;
import com.laziton.mlbetaone.data.MovieLockerSqlContext;

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
		Cursor cursor = this.database.query(MovieLockerSqlContext.MOVIE_TABLE_NAME, 
											null, 
											MovieLockerSqlContext.MOVIE_ID + "=?", 
											new String[]{ String.valueOf(id) }, 
											null, 
											null, 
											null, 
											"1");
		
		if(cursor.moveToFirst()){
			movie = new Movie();
			
			movie.setId(cursor.getInt(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_ID)));
			movie.setName(cursor.getString(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_NAME)));
			movie.setDescription(cursor.getString(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_DESCRIPTION)));
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
		Cursor cursor = this.GetMovieCursor();
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		if(cursor.moveToFirst()){
			do {
				Movie movie = new Movie();
				
				movie.setId(cursor.getInt(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_ID)));
				movie.setName(cursor.getString(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_NAME)));
				movie.setDescription(cursor.getString(cursor.getColumnIndex(MovieLockerSqlContext.MOVIE_DESCRIPTION)));
				
				result.add(movie);
			} while(cursor.moveToNext());
		}
		
		return result;
	}

	@Override
	public void InsertMovie(Movie movie) {
		ContentValues values = new ContentValues();
		values.put(MovieLockerSqlContext.MOVIE_ID, movie.getId());
		values.put(MovieLockerSqlContext.MOVIE_NAME, movie.getName());
		values.put(MovieLockerSqlContext.MOVIE_DESCRIPTION, movie.getDescription());
		
		movie.setId((int)this.database.insert(MovieLockerSqlContext.MOVIE_TABLE_NAME, null, values));
	}

	@Override
	public void UpdateMovie(Movie movie) {
		ContentValues values = new ContentValues();
		values.put(MovieLockerSqlContext.MOVIE_ID, movie.getId());
		values.put(MovieLockerSqlContext.MOVIE_NAME, movie.getName());
		values.put(MovieLockerSqlContext.MOVIE_DESCRIPTION, movie.getDescription());
		
		this.database.update(MovieLockerSqlContext.MOVIE_TABLE_NAME, values, MovieLockerSqlContext.MOVIE_ID + "=?", new String[]{ String.valueOf(movie.getId()) });
	}

	@Override
	public void DeleteMovie(Movie movie) {
		this.database.delete(MovieLockerSqlContext.MOVIE_TABLE_NAME, MovieLockerSqlContext.MOVIE_ID + "=?", new String[]{ String.valueOf(movie.getId()) });
	}
}
