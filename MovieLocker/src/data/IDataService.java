package data;

import java.util.ArrayList;
import java.util.List;

import DataModels.Image;
import DataModels.Movie;
import android.database.Cursor;

public interface IDataService {
	public Cursor GetMovieCursor();
	public ArrayList<Movie> GetMovies();
	
	public Cursor GetMovieCursor(String name, Integer genre, Integer collection);
	public ArrayList<Movie> GetMovies(String name, Integer genre, Integer collection);
	
	public Image GetImage(int imageId);
	public ArrayList<Image> GetImages(List<Integer> imageIds);
}
