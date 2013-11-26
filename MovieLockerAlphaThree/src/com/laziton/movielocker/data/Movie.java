package com.laziton.movielocker.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class Movie implements Serializable {
	@DatabaseField(columnName = MovieLockerSqlContext.MOVIE_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_NAME)
	private String name;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_PRICE)
	private Double price;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_OWNED)
	private boolean owned;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_GENRE_ID)
	private int genreId;
	
	public Movie(){
		this.name = "";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean getOwned(){
		return this.owned;
	}
	
	public void setOwned(boolean owned){
		this.owned = owned;
	}
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public int getGenreId() {
		return genreId;
	}
	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}
}
