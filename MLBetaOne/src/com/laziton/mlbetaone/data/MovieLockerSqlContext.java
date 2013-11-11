package com.laziton.mlbetaone.data;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Bundle;

public class MovieLockerSqlContext extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "MovieLocker.db";
	private static final int DATABASE_V1 = 1;
	
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
	
	public static final String IMAGE_TABLE_NAME = "Image";
	public static final String IMAGE_ID = "_id";
	public static final String IMAGE_IMAGE_DATA = "ImageData";
	private static final String IMAGE_TABLE_CREATE = "create table " + IMAGE_TABLE_NAME
														+ "(" + IMAGE_ID + " integer primary key autoincrement, "
														+ IMAGE_IMAGE_DATA + " blob);";
	
	public static final String MOVIE_TABLE_NAME = "Movie";
	public static final String MOVIE_ID = "_id";
	public static final String MOVIE_NAME = "Name";
	public static final String MOVIE_DESCRIPTION = "Description";
	public static final String MOVIE_IMAGE_ID = "ImageName";
	public static final String MOVIE_PRICE = "Price";
	public static final String MOVIE_GENRE_ID = "GenreId";
	private static final String MOVIE_TABLE_CREATE = "create table " + MOVIE_TABLE_NAME
														+ "(" + MOVIE_ID + " integer primary key autoincrement, "
														+ MOVIE_NAME + " text not null, "
														+ MOVIE_DESCRIPTION + " text null"
//														+ MOVIE_IMAGE_ID + " integer null, "
//														+ MOVIE_PRICE + " real null, "
//														+ MOVIE_GENRE_ID + " integer not null, "
//														+ "foreign key(" + MOVIE_IMAGE_ID + ") references " + IMAGE_TABLE_NAME + "(" + IMAGE_ID + "), "
//														+ "foreign key(" + MOVIE_GENRE_ID + ") references " + GENRE_TABLE_NAME + "(" + GENRE_ID + ")"
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
	
	public MovieLockerSqlContext(Context context){
		super(context, DATABASE_NAME, null, DATABASE_V1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
//		database.execSQL(GENRE_TABLE_CREATE);
//		database.execSQL(COLLECTION_TABLE_CREATE);
//		database.execSQL(IMAGE_TABLE_CREATE);
		database.execSQL(MOVIE_TABLE_CREATE);
//		database.execSQL(COLLECTIONMOVIE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
