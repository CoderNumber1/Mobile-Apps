package com.laziton.movielocker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.images.ImageManager;
import com.laziton.movielocker.images.ImageManager.GetImageAsyncCallback;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MovieActivity extends SingleFragmentHost {
	public static final String MOVIE_ID = "com.laziton.movielocker.MovieActivity.movieId";
	public static final int ACTIVITY_GET_IMAGE = 1;
	public static final int ACTIVITY_GET_BARCODE = 2;

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(MovieActivity.MOVIE_ID, -1);
		return MovieFragment.newInstance(id);
	}

	public static class MovieFragment extends Fragment{
		Movie movie;
		Uri movieImageTempUri;
		Bitmap movieImage;
		
		EditText txtName;
		EditText txtDescription;
		EditText txtBarcode;
		Spinner spnGenre;
		ImageButton btnTakePicture;
		ImageView movieImageView;
		Button btnScanBarcode;
		
		public static MovieFragment newInstance(int id){
			MovieFragment result = new MovieFragment();
			Bundle args = new Bundle();
	        args.putSerializable(MovieActivity.MOVIE_ID, id);
	        result.setArguments(args);
			return result;
		}
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        int id = getArguments().getInt(MovieActivity.MOVIE_ID);
	        if(id > 0){
		        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
		        dataService.Open();
		        this.movie = dataService.GetMovie(id);
		        dataService.Close();
	        }
	        else{
	        	this.movie = new Movie();
	        }
	        
	        setHasOptionsMenu(true);
	    }
		
		@TargetApi(11)
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.movie_fragment, parent, false);
	  
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	        }   
	        
	        this.movieImageView = (ImageView)view.findViewById(R.id.imgMoviePhoto);
	        
	        IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	        dataService.Open();
	        ArrayList<Genre> genres = dataService.GetGenres();
	        GenreAdapter adapter = new GenreAdapter(genres); 
	        spnGenre = (Spinner)view.findViewById(R.id.spnGenre);
	        spnGenre.setAdapter(adapter);
	        for(Genre g : genres){if(g.getId() == movie.getGenreId()){spnGenre.setSelection(adapter.getPosition(g));break;}}
	        spnGenre.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> adapter, View view, int pos, long arg3) {
					Genre selectedGenre = (Genre)adapter.getItemAtPosition(pos);
					movie.setGenreId(selectedGenre.getId());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
	        });
	        
	        txtName = (EditText)view.findViewById(R.id.txtName);
	        txtName.setText(movie.getName());
	        txtName.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	                movie.setName(c.toString());
	            }

	            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
	                // this space intentionally left blank
	            }

	            public void afterTextChanged(Editable c) {
	                // this one too
	            }
	        });
	        txtDescription = (EditText)view.findViewById(R.id.txtDescription);
	        txtDescription.setText(movie.getDescription());
	        txtDescription.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	                movie.setDescription(c.toString());
	            }

	            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
	                // this space intentionally left blank
	            }

	            public void afterTextChanged(Editable c) {
	                // this one too
	            }
	        });
	        
	        this.txtBarcode = (EditText)view.findViewById(R.id.txtBarcode);
	        
	        btnTakePicture = (ImageButton)view.findViewById(R.id.btnTakePicture);
	        btnTakePicture.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String imageName = "movie_locker_" + String.valueOf(UUID.randomUUID()) + ".jpg";
					File imageFile = new File(getActivity().getFilesDir(), imageName);
					imageFile.delete();
					try {
						FileOutputStream fileOut = getActivity().openFileOutput(imageName, Context.MODE_WORLD_WRITEABLE);
						fileOut.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					imageFile = new File(getActivity().getFilesDir(), imageName);
					
					movieImageTempUri = Uri.fromFile(imageFile);
					List<Intent> cameraIntents = new ArrayList<Intent>();
					Intent getCameraImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					PackageManager pm = getActivity().getPackageManager();
					for(ResolveInfo res : pm.queryIntentActivities(getCameraImage, 0)){
						final String packageName = res.activityInfo.packageName;
						final Intent cameraIntent = new Intent(getCameraImage);
						cameraIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
						cameraIntent.setPackage(packageName);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, movieImageTempUri);
						getCameraImage.putExtra("return-data", true);
						cameraIntents.add(cameraIntent);
					}
					
					Intent imageSelectIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					
					final Intent chooser = Intent.createChooser(imageSelectIntent, "Complete action using");
					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,  cameraIntents.toArray(new Parcelable[]{}));
					
					startActivityForResult(chooser, ACTIVITY_GET_IMAGE);
				}
			});
	        
	        this.btnScanBarcode = (Button)view.findViewById(R.id.btnScanBarcode);
	        this.btnScanBarcode.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent scanIntent = new Intent(getActivity(), ScannerActivity.class);
					startActivityForResult(scanIntent, MovieActivity.ACTIVITY_GET_BARCODE);
				}
			});
	        
	        return view; 
	    }
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode == RESULT_OK){
				if(requestCode == MovieActivity.ACTIVITY_GET_BARCODE){
					String barcode = data.getStringExtra(ScannerActivity.BARCODE_KEY);
					this.txtBarcode.setText(barcode);
					new BarcodeSearchTask(new BarcodeSearchAsyncCallback(){

						@Override
						public void callback(JSONObject result) {
							try {
								JSONObject basic = result.getJSONObject("basic");
								String name = basic.getString("name");
								txtName.setText(name);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}).execute(barcode);
				}
				else if(requestCode == MovieActivity.ACTIVITY_GET_IMAGE){
					boolean isCamera;
					if(data == null)
						isCamera = true;
					else{
						String action = data.getAction();
						if(action == null)
							isCamera = false;
						else
							isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					}
					
					Uri selectedImage;
					if(isCamera)
						selectedImage = this.movieImageTempUri;
					else
						selectedImage = data == null ? null : data.getData();
					
					if(selectedImage != null){
						this.movie.setImageUri(selectedImage.toString());
						this.setImage(selectedImage);
					}
				}
			}
		}
		
		private void setImage(Uri imageUri){
			ImageManager.getInstance().getImageAsync(imageUri, new ImageManager.GetImageAsyncCallback() {
				
				@Override
				public void callback(BitmapDrawable image) {
					MovieFragment.this.movieImageView.setImageDrawable(image);
				}
			});
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.crud_option_menu, menu);
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	
	        switch (item.getItemId()) {
	            case R.id.genre_menu_save:
	            	if(this.movie.getId() > 0)
	            		dataService.UpdateMovie(this.movie);
	            	else
	            		dataService.InsertMovie(this.movie);
	            	
	            	dataService.Close();
	            	break;
	            case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	        
	        return true;
	    }
		
		@Override
		public void onStart(){
			super.onStart();
			if(this.movie.getImageUri() != null)
				this.setImage(Uri.parse(this.movie.getImageUri()));
		}
		
		@Override
		public void onStop(){
			super.onStop();
			ImageManager.getInstance().cleanImageView(this.movieImageView);
		}
		
		private class GenreAdapter extends ArrayAdapter<Genre> {
	        public GenreAdapter(ArrayList<Genre> genres) {
	            super(getActivity(), android.R.layout.simple_list_item_1, genres);
	        }

	        @Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
	            // if we weren't given a view, inflate one
	            if (null == convertView) {
	                convertView = getActivity().getLayoutInflater()
	                    .inflate(android.R.layout.simple_list_item_1, null);
	            }

	            // configure the view for this Crime
	            Genre contact = getItem(position);

	            TextView titleTextView =
	                (TextView)convertView.findViewById(android.R.id.text1);
	            titleTextView.setText(contact.getName());

	            return convertView;
			}

			@Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            // if we weren't given a view, inflate one
	            if (null == convertView) {
	                convertView = getActivity().getLayoutInflater()
	                    .inflate(android.R.layout.simple_list_item_1, null);
	            }

	            // configure the view for this Crime
	            Genre contact = getItem(position);

	            TextView titleTextView =
	                (TextView)convertView.findViewById(android.R.id.text1);
	            titleTextView.setText(contact.getName());

	            return convertView;
	        }
	    }
		
		public interface BarcodeSearchAsyncCallback{
			public void callback(JSONObject result);
		}
		
		private class BarcodeSearchTask extends AsyncTask<String,Integer,JSONObject>{
			BarcodeSearchAsyncCallback callback;
			
			public BarcodeSearchTask(BarcodeSearchAsyncCallback callback){
				this.callback = callback;
			}
			
			@Override
			protected JSONObject doInBackground(String... barcode) {
				JSONObject result = null;
		    	HttpClient client = new DefaultHttpClient();
		    	HttpResponse response;
		        String responseString = null;
		    	try {
		    		String url = String.format("https://api.scandit.com/v2/products/%s?key=%s", barcode[0], "bK5Ky4GYpvc_hD56elkfJnGfAEIYO5QU0dDKO0RJLYp");
		            response = client.execute(new HttpGet(url));
		            StatusLine statusLine = response.getStatusLine();
		            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		            	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
						StringBuilder builder = new StringBuilder();
						for (String line = null; (line = reader.readLine()) != null; ) {
						    builder.append(line).append("\n");
						}
						JSONTokener tokener = new JSONTokener(builder.toString());
						result = new JSONObject(tokener);
		            } else{
		                //Closes the connection.
		                response.getEntity().getContent().close();
		                throw new IOException(statusLine.getReasonPhrase());
		            }
		        } catch (ClientProtocolException e) {
		            //TODO Handle problems..
		        } catch (IOException e) {
		            //TODO Handle problems..
		        } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	return result;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				this.callback.callback(result);
			}
			
		}
	}
}
