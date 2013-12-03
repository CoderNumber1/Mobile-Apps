package com.laziton.movielocker.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class Collection implements Serializable {
	@DatabaseField(columnName = MovieLockerSqlContext.COLLECTION_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName = MovieLockerSqlContext.COLLECTION_NAME)
	private String name;
	
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
}
