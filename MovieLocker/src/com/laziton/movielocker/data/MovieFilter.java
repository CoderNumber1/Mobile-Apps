package com.laziton.movielocker.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable()
public class MovieFilter implements Serializable {
	@DatabaseField(columnName = MovieLockerSqlContext.MOVIEFILTER_ID, generatedId=true)
	private int id;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_FILTER_NAME)
	private String filterName;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_MOVIE_NAME)
	private String movieName;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_GENRE_IDS)
	private String genreIds;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_COLLECTION_IDS)
	private String collectionIds;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_WISH_LIST)
	private boolean wishList;
	@DatabaseField(columnName=MovieLockerSqlContext.MOVIEFILTER_OWNED)
	private boolean owned;
	
	public MovieFilter(){
		this.filterName = "";
		this.movieName = "";
		this.genreIds = "";
		this.collectionIds = "";
	}
	
	public boolean isCleared(){
		return (this.getMovieName() == null || this.getMovieName().equals(""))
				&&(this.getGenreIds() == null || this.getGenreIds().equals(""))
				&& (this.getCollectionIds() == null || this.getCollectionIds().equals(""))
				&& (this.isOwned() == this.isWishList());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(String genreIds) {
		this.genreIds = genreIds;
	}

	public String getCollectionIds() {
		return collectionIds;
	}

	public void setCollectionIds(String collectionIds) {
		this.collectionIds = collectionIds;
	}

	public boolean isWishList() {
		return wishList;
	}

	public void setWishList(boolean wishList) {
		this.wishList = wishList;
	}

	public boolean isOwned() {
		return owned;
	}

	public void setOwned(boolean owned) {
		this.owned = owned;
	}
}
