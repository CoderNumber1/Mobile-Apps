package com.laziton.movielocker.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class CollectionMovie {
	@DatabaseField(columnName = MovieLockerSqlContext.COLLECTIONMOVIE_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName = MovieLockerSqlContext.COLLECTIONMOVIE_COLLECTION_ID)
	private int collectionId;
	@DatabaseField(columnName = MovieLockerSqlContext.COLLECTIONMOVIE_MOVIE_ID)
	private int movieId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
}
