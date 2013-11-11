package com.laziton.mlalphatwo.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable()
public class Genre {
	@DatabaseField(columnName = "_id", generatedId=true)
	private int id;
	@DatabaseField(columnName="Name")
	private String name;
	
	public Genre(){
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
}
