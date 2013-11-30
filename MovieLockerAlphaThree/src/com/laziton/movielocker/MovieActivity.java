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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.laziton.mlalphathree.R;
import com.laziton.movielocker.CollectionActivity.CollectionFragment;
import com.laziton.movielocker.data.Collection;
import com.laziton.movielocker.data.CollectionMovie;
import com.laziton.movielocker.data.Genre;
import com.laziton.movielocker.data.Movie;
import com.laziton.movielocker.dataservices.DataServiceFactory;
import com.laziton.movielocker.dataservices.IDataService;
import com.laziton.movielocker.images.ImageManager;
import com.laziton.movielocker.images.ImageManager.GetImageAsyncCallback;
import com.laziton.movielocker.images.ImageManager.SetImageAsyncCallback;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MovieActivity extends SingleFragmentHost {
	public static final String MOVIE_ID = "com.laziton.movielocker.MovieActivity.movieId";
	public static final int ACTIVITY_GET_IMAGE = 1;
	public static final int ACTIVITY_GET_BARCODE = 2;
	public static final int ACTIVITY_GET_COLLECTIONS = 3;

	@Override
	protected Fragment createFragment() {
		int id = getIntent().getIntExtra(MovieActivity.MOVIE_ID, -1);
		return MovieFragment.newInstance(id);
	}

	public static class MovieFragment extends Fragment{
		Movie movie;
		ArrayList<Collection> movieCollections;
		ArrayAdapter<Collection> collectionsAdapter;
		Uri movieImageTempUri;
		Bitmap movieImage;
		
		EditText txtName;
		EditText txtPrice;
		EditText txtBarcode;
		Spinner spnGenre;
		ImageButton btnTakePicture;
		ImageView movieImageView;
		Button btnScanBarcode;
		ListView lstMembers;
		Button btnEditMembers;
		
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
		        this.movieCollections = dataService.GetCollectionsByCollectionMovies(dataService.GetCollectionMovies(null, id));
		        dataService.Close();
	        }
	        else{
	        	this.movie = new Movie();
	        	this.movieCollections = new ArrayList<Collection>();
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
	        txtPrice = (EditText)view.findViewById(R.id.txtPrice);
	        txtPrice.setText(movie.getPrice() != null ? movie.getPrice().toString() : null);
	        txtPrice.addTextChangedListener(new TextWatcher() {
	            public void onTextChanged(CharSequence c, int start, int before, int count) {
	            	if(!c.toString().equals("")){
	            		movie.setPrice(Double.parseDouble(c.toString()));
	            	}
	            	else{
	            		movie.setPrice(null);
	            	}

//	                movie.setDescription(c.toString());
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
					movieImageTempUri = ImageManager.getInstance().generateTempUri();
					List<Intent> cameraIntents = new ArrayList<Intent>();
					Intent getCameraImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					PackageManager pm = getActivity().getPackageManager();
					for(ResolveInfo res : pm.queryIntentActivities(getCameraImage, 0)){
						final String packageName = res.activityInfo.packageName;
						final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						cameraIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
						cameraIntent.setPackage(packageName);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, movieImageTempUri);
						cameraIntent.putExtra("return-data", true);
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
	        
	        this.lstMembers = (ListView)view.findViewById(R.id.lstMembers);
	        collectionsAdapter = new ArrayAdapter<Collection>(getActivity(), android.R.layout.simple_list_item_1, this.movieCollections){
	        	@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            if (null == convertView) {
		                convertView = getActivity().getLayoutInflater()
		                    .inflate(android.R.layout.simple_list_item_1, null);
		            }

		            Collection collection = getItem(position);
		            TextView titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
		            titleTextView.setText(collection.getName());

		            return convertView;
				}
	        };
	        this.lstMembers.setAdapter(collectionsAdapter);
	        
	        this.btnEditMembers = (Button)view.findViewById(R.id.btnEditMembers);
	        if(this.movie.getId() <= 0)
	        	this.btnEditMembers.setActivated(false);
	        this.btnEditMembers.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent getMembers = new Intent(getActivity(), MovieCollectionsActivity.class);
					getMembers.putExtra(MOVIE_ID, movie.getId());
					MovieFragment.this.startActivityForResult(getMembers, ACTIVITY_GET_COLLECTIONS);
				}
			});
	        
	        RadioButton rdoOwned = (RadioButton)view.findViewById(R.id.rdoOwned);
	        RadioButton rdoWish = (RadioButton)view.findViewById(R.id.rdoWish); 
	        
	        if(this.movie.getOwned()){
	        	rdoOwned.setChecked(true);
	        	rdoWish.setChecked(false);
	        }
	        else{
	        	rdoOwned.setChecked(false);
	        	rdoWish.setChecked(true);
	        }
	        
	        rdoOwned.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MovieFragment.this.onRadioClicked(v);
				}
			});
	        
	        rdoWish.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MovieFragment.this.onRadioClicked(v);
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
					new BBYOpenBarcodeSearchTask(new BarcodeSearchAsyncCallback(){

						
						@Override
						public void callback(BarcodeSearchResult result) {
							JSONArray products;
							try {
								products = result.result.getJSONArray("products");
								if(products != null && products.length() > 0){
									JSONObject product = products.getJSONObject(0);
									String name = product.getString("name");
									Double price = product.getDouble("salePrice");
									txtPrice.setText(price.toString());
									txtName.setText(name);
								}
								else{
									new ScanditBarcodeSearchTask(new BarcodeSearchAsyncCallback(){

										@Override
										public void callback(BarcodeSearchResult result) {
											try {
												JSONObject basic = result.result.getJSONObject("basic");
												String name = basic.getString("name");
												txtName.setText(name);
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
										
									}).execute(result.barcode);
								}
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
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
						this.setImage(selectedImage);
					}
				}
				else if(requestCode == MovieActivity.ACTIVITY_GET_COLLECTIONS){
					Bundle args = data.getExtras();
					Object result = args.getSerializable(MovieCollectionsActivity.EXTRA_SELECTED_COLLECTIONS);
					ArrayList<Collection> members = (ArrayList<Collection>)result;
					IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
					dataService.Open();
					dataService.DeleteCollectionMovies(dataService.GetCollectionMovies(null, this.movie.getId()));
					for(Collection member : members){
						CollectionMovie memberEntry = new CollectionMovie();
						memberEntry.setMovieId(this.movie.getId());
						memberEntry.setCollectionId(member.getId());
						dataService.InsertCollectionMovie(memberEntry);
					}
					this.movieCollections.clear(); 
					this.movieCollections.addAll(dataService.GetCollectionsByCollectionMovies(((dataService.GetCollectionMovies(null, this.movie.getId())))));
					this.collectionsAdapter.notifyDataSetChanged();
					dataService.Close();
				}
			}
		}
		
		private void setImage(Uri imageUri){
			this.movieImageTempUri = imageUri;
			ImageManager.getInstance().getImageAsync(imageUri, new ImageManager.GetImageAsyncCallback() {
				
				@Override
				public void callback(BitmapDrawable image) {
					MovieFragment.this.movieImageView.setImageDrawable(image);
				}
			});
		}
		
		private void setImage(int movieId){
			ImageManager.getInstance().getImageAsync(movieId, new ImageManager.GetImageAsyncCallback() {
				
				@Override
				public void callback(BitmapDrawable image) {
					if(image != null)
						MovieFragment.this.movieImageView.setImageDrawable(image);
				}
			});
		}
		
		public void onRadioClicked(View view){
			boolean checked = ((RadioButton)view).isChecked();
			
			switch(view.getId()){
				case R.id.rdoOwned:
					if(checked)
						this.movie.setOwned(true);
					break;
				case R.id.rdoWish:
					if(checked)
						this.movie.setOwned(false);
					break;
			}
		}

		@Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.main, menu);
	        menu.findItem(R.id.menu_add).setVisible(false);
	        menu.findItem(R.id.menu_done).setVisible(false);
	        menu.findItem(R.id.menu_filter).setVisible(false);
	        this.getActivity().invalidateOptionsMenu();
	    }
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			IDataService dataService = DataServiceFactory.GetInstance().GetDataService();
	    	dataService.Open();
	    	
	        switch (item.getItemId()) {
	            case R.id.menu_save:
	            	if(this.movie.getId() > 0)
	            		dataService.UpdateMovie(this.movie);
	            	else
	            		dataService.InsertMovie(this.movie);
	            	
	            	if(this.movieImageTempUri != null)
	            		ImageManager.getInstance().setImageAsync(this.movie.getId(), this.movieImageTempUri, new SetImageAsyncCallback(){
							@Override
							public void callback(Boolean success) {
								if(success)
									ImageManager.getInstance().cleanupTempImages();
							}
	            		});
	            	
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
			if(this.movie.getId() > 0 && this.movieImageTempUri == null)
				this.setImage(this.movie.getId());
		}
		
		@Override
		public void onStop(){
			super.onStop();
			ImageManager.getInstance().cleanImageView(this.movieImageView);
			ImageManager.getInstance().cleanupTempImages();
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
		
		public class BarcodeSearchResult{
			public String barcode;
			public JSONObject result;
		}
		
		public interface BarcodeSearchAsyncCallback{
			public void callback(BarcodeSearchResult result);
		}
		
		private class ScanditBarcodeSearchTask extends AsyncTask<String,Integer,BarcodeSearchResult>{
			BarcodeSearchAsyncCallback callback;
			
			public ScanditBarcodeSearchTask(BarcodeSearchAsyncCallback callback){
				this.callback = callback;
			}
			
			@Override
			protected BarcodeSearchResult doInBackground(String... barcode) {
				BarcodeSearchResult result = new BarcodeSearchResult();
				result.barcode = barcode[0];
				JSONObject resultObject = null;
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
						resultObject = new JSONObject(tokener);
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
		    	
		    	result.result = resultObject;
		    	return result;
			}

			@Override
			protected void onPostExecute(BarcodeSearchResult result) {
				this.callback.callback(result);
			}
			
		}
		
		private class BBYOpenBarcodeSearchTask extends AsyncTask<String,Integer,BarcodeSearchResult>{
			BarcodeSearchAsyncCallback callback;
			
			public BBYOpenBarcodeSearchTask(BarcodeSearchAsyncCallback callback){
				this.callback = callback;
			}
			
			@Override
			protected BarcodeSearchResult doInBackground(String... barcode) {
				BarcodeSearchResult result = new BarcodeSearchResult();
				result.barcode = barcode[0];
				JSONObject resultObject = null;
		    	HttpClient client = new DefaultHttpClient();
		    	HttpResponse response;
		        String responseString = null;
		    	try {
		    		String url = String.format("http://api.remix.bestbuy.com/v1/products(upc=%s)?show=name,salePrice&apiKey=%s&format=json", barcode[0], "p2a5rm9yg2cvsxggkcp8mpd6");
//		    		String url = String.format("https://api.scandit.com/v2/products/%s?key=%s", barcode[0], "bK5Ky4GYpvc_hD56elkfJnGfAEIYO5QU0dDKO0RJLYp");
		            response = client.execute(new HttpGet(url));
		            StatusLine statusLine = response.getStatusLine();
		            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		            	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
						StringBuilder builder = new StringBuilder();
						for (String line = null; (line = reader.readLine()) != null; ) {
						    builder.append(line).append("\n");
						}
						JSONTokener tokener = new JSONTokener(builder.toString());
						resultObject = new JSONObject(tokener);
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
		    	
		    	result.result = resultObject;
		    	return result;
			}

			@Override
			protected void onPostExecute(BarcodeSearchResult result) {
				this.callback.callback(result);
			}
			
		}
	}
}
