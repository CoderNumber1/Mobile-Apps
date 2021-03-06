package com.laziton.mlalphatwo.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class Movie {
	@DatabaseField(columnName = MovieLockerSqlContext.MOVIE_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_NAME)
	private String name;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIE_DESCRIPTION)
	private String description;
	private String imageName;
	private double price;
	@DatabaseField(columnName="GenreId")
	private int genreId;
	
	public Movie(){
		this.name = "";
		this.description = "";
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getGenreId() {
		return genreId;
	}
	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}
}
