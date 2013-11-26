package com.laziton.movielocker.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class Image {
	@DatabaseField(columnName = MovieLockerSqlContext.IMAGE_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName = MovieLockerSqlContext.IMAGE_MOVIE_ID)
	private int movieId;
	@DatabaseField(columnName = MovieLockerSqlContext.IMAGE_IMAGE_DATA, dataType=DataType.BYTE_ARRAY)
	private byte[] imageData;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMovieId(){
		return this.movieId;
	}
	public void setMovieId(int movieId){
		this.movieId = movieId;
	}
	
	public byte[] getImageData() {
		return this.imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
}
