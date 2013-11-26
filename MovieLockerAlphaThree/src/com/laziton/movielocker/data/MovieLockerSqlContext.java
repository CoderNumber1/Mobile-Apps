package com.laziton.movielocker.data;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MovieLockerSqlContext extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "MovieLocker.db";
	private static final int DATABASE_V1 = 1;
	private static final int DATABASE_V2 = 2;
	private static final int DATABASE_V3 = 3;
	private static final int DATABASE_V4 = 4;
	private static final int DATABASE_V5 = 5;
	private static final int DATABASE_V6 = 6;
	private static final int DATABASE_V7 = 7;
	private static final int DATABASE_V8 = 8;
	private static final int DATABASE_VERSION = DATABASE_V8;
	
	private Dao<CollectionMovie,Integer> collectionMovieDao = null;
	private Dao<Collection,Integer> collectionDao = null;
	private Dao<Image,Integer> imageDao = null;
	private Dao<Movie,Integer> movieDao = null;
	private Dao<Genre,Integer> genreDao = null;
	private Dao<MovieFilter,Integer> movieFilterDao = null;
	
	public static final String GENRE_TABLE_NAME = "Genre";
	public static final String GENRE_ID = "_id";
	public static final String GENRE_NAME = "Name";
	private static final String GENRE_TABLE_CREATE = "create table " + GENRE_TABLE_NAME
														+ " ("
														+ GENRE_ID + " integer primary key autoincrement, "
														+ GENRE_NAME + " text not null"
														+ ");";
	
	public static final String COLLECTION_TABLE_NAME = "Collection";
	public static final String COLLECTION_ID = "_id";
	public static final String COLLECTION_NAME = "Name";
	private static final String COLLECTION_TABLE_CREATE = "create table " + COLLECTION_TABLE_NAME
														+ " ("
														+ COLLECTION_ID + " integer primary key autoincrement, "
														+ COLLECTION_NAME + " text not null"
														+ ");";
	
	public static final String MOVIE_TABLE_NAME = "Movie";
	public static final String MOVIE_ID = "_id";
	public static final String MOVIE_NAME = "Name";
	public static final String MOVIE_DESCRIPTION = "Description";
	public static final String MOVIE_PRICE = "Price";
	public static final String MOVIE_OWNED = "Owned";
	public static final String MOVIE_GENRE_ID = "GenreId";
	private static final String MOVIE_TABLE_CREATE = "create table " + MOVIE_TABLE_NAME
														+ "(" + MOVIE_ID + " integer primary key autoincrement, "
														+ MOVIE_NAME + " text not null, "
//														+ MOVIE_DESCRIPTION + " text null, "
														+ MOVIE_PRICE + " real null, "
														+ MOVIE_OWNED + " integer not null, "
														+ MOVIE_GENRE_ID + " integer not null, "
//														+ "foreign key(" + MOVIE_IMAGE_ID + ") references " + IMAGE_TABLE_NAME + "(" + IMAGE_ID + "), "
														+ "foreign key(" + MOVIE_GENRE_ID + ") references " + GENRE_TABLE_NAME + "(" + GENRE_ID + ")"
														+ ");";
	
	public static final String IMAGE_TABLE_NAME = "Image";
	public static final String IMAGE_ID = "_id";
	public static final String IMAGE_IMAGE_DATA = "ImageData";
	public static final String IMAGE_MOVIE_ID = "MovieId";
	private static final String IMAGE_TABLE_CREATE = "create table " + IMAGE_TABLE_NAME
														+ "(" + IMAGE_ID + " integer primary key autoincrement, "
														+ IMAGE_IMAGE_DATA + " blob, "
														+ IMAGE_MOVIE_ID + " integer not null, "
														+ "foreign key(" + IMAGE_MOVIE_ID + ") references " + MOVIE_TABLE_NAME + "(" + MOVIE_ID + ")"
														+ ");";
	
	public static final String COLLECTIONMOVIE_TABLE_NAME = "CollectionMovie";
	public static final String COLLECTIONMOVIE_ID = "_id";
	public static final String COLLECTIONMOVIE_COLLECTION_ID = "CollectionId";
	public static final String COLLECTIONMOVIE_MOVIE_ID = "MovieId";
	private static final String COLLECTIONMOVIE_TABLE_CREATE  = "create table " + COLLECTIONMOVIE_TABLE_NAME
																	+ "(" + COLLECTIONMOVIE_ID + " integer primary key autoincrement, "
																	+ COLLECTIONMOVIE_COLLECTION_ID + " integer not null, "
																	+ COLLECTIONMOVIE_MOVIE_ID + " integer not null, "
																	+ "unique(" + COLLECTIONMOVIE_COLLECTION_ID + "," + COLLECTIONMOVIE_MOVIE_ID + "), "
																	+ "foreign key(" + COLLECTIONMOVIE_COLLECTION_ID + ") references " + COLLECTION_TABLE_NAME + "(" + COLLECTION_ID + "), "
																	+ "foreign key(" + COLLECTIONMOVIE_MOVIE_ID + ") references " + MOVIE_TABLE_NAME + "(" + MOVIE_ID + "));";
	
	public static final String MOVIEFILTER_TABLE_NAME = "MovieFilter";
	public static final String MOVIEFILTER_ID = "_id";
	public static final String MOVIEFILTER_FILTER_NAME = "FilterName";
	public static final String MOVIEFILTER_MOVIE_NAME = "MovieName";
	public static final String MOVIEFILTER_GENRE_IDS = "GenreIds";
	public static final String MOVIEFILTER_COLLECTION_IDS = "CollectionIds";
	public static final String MOVIEFILTER_WISH_LIST = "WishList";
	public static final String MOVIEFILTER_OWNED = "Owned";
	private static final String MOVIEFILTER_TABLE_CREATE = "create table " + MOVIEFILTER_TABLE_NAME
																+ "(" + MOVIEFILTER_ID + " integer primary key autoincrement, "
																+ MOVIEFILTER_FILTER_NAME + " text not null, "
																+ MOVIEFILTER_MOVIE_NAME + " text null, "
																+ MOVIEFILTER_GENRE_IDS + " text null, "
																+ MOVIEFILTER_COLLECTION_IDS + " text null, "
																+ MOVIEFILTER_WISH_LIST + " integer not null, "
																+ MOVIEFILTER_OWNED + " integer not null);";
	
	public MovieLockerSqlContext(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		database.execSQL(GENRE_TABLE_CREATE);
		database.execSQL(MOVIE_TABLE_CREATE);
		database.execSQL(IMAGE_TABLE_CREATE);
		database.execSQL(COLLECTION_TABLE_CREATE);
		database.execSQL(COLLECTIONMOVIE_TABLE_CREATE);
		database.execSQL(MOVIEFILTER_TABLE_CREATE);
		 
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Action')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Adventure')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Animation')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Comedy')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Crime')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Erotica')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Faction')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Fantasy')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Historical')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Live-Action Scripted')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Live-Action Un-Scripted')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Mystery')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Paranoid')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Philosophical')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Political')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Romance')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Saga')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Satire')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Science Fiction')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Slice of Life')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Speculative')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Thriller')");
		database.execSQL("insert into " + GENRE_TABLE_NAME + "(" + GENRE_NAME + ") values('Urban')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.COLLECTIONMOVIE_TABLE_NAME + "';");
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.COLLECTION_TABLE_NAME + "';");
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.IMAGE_TABLE_NAME + "';");
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.MOVIE_TABLE_NAME + "';");
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.GENRE_TABLE_NAME + "';");
		database.execSQL("drop table if exists '" + MovieLockerSqlContext.MOVIEFILTER_TABLE_NAME + "';");
		
		this.onCreate(database, connectionSource);
	}
	
	public Dao<MovieFilter,Integer> getMovieFilterDao(){
		if(this.movieFilterDao == null){
			try {
				this.movieFilterDao = getDao(MovieFilter.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.movieFilterDao;
	}
	
	public Dao<CollectionMovie,Integer> getCollectionMovieDao(){
		if(this.collectionMovieDao == null){
			try {
				this.collectionMovieDao = getDao(CollectionMovie.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.collectionMovieDao;
	}
	
	public Dao<Collection,Integer> getCollectionDao(){
		if(this.collectionDao == null){
			try{
				this.collectionDao = getDao(Collection.class);
			}
			catch(java.sql.SQLException ex){
				//TODO
			}
		}
		
		return this.collectionDao;
	}
	
	public Dao<Image,Integer> getImageDao(){
		if(this.imageDao == null){
			try{
				this.imageDao = getDao(Image.class);
			}
			catch(java.sql.SQLException ex){
				Log.e("SqlContext", ex.toString());
				ex.printStackTrace();
			}
		}
		
		return this.imageDao;
	}
	
	public Dao<Movie,Integer> getMovieDao(){
		if(this.movieDao == null){
			try{
				this.movieDao = getDao(Movie.class);
			}
			catch(java.sql.SQLException ex){
				//TODO
			}
		}
		
		return this.movieDao;
	}
	
	public Dao<Genre,Integer> getGenreDao(){
		if(this.genreDao == null){
			try{
				this.genreDao = getDao(Genre.class);
			}
			catch(java.sql.SQLException ex){
				//TODO
			}
		}
		
		return this.genreDao;
	}
}
