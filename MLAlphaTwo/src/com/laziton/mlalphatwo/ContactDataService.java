package com.laziton.mlalphatwo;
//package com.laziton.mlbetaone;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.UUID;
//
//import org.json.JSONArray;
//import org.json.JSONTokener;
//
//import android.content.Context;
//import android.util.Log;
//
//public class ContactDataService {
//	private static final String FILENAME = "contacts.json";
//	private static final String TAG = "ContactDataService";
//	
//    private ArrayList<Contact> contacts;
//
//    private static ContactDataService dataService;
//    private Context context;
//
//    private ContactDataService(Context appContext) {
//        this.context = appContext;
//        contacts = new ArrayList<Contact>();
//    	this.LoadContacts();
//    }
//
//    public static ContactDataService get(Context context) {
//        if (dataService == null) {
//            dataService = new ContactDataService(context.getApplicationContext());
//        }
//        return dataService;
//    }
//
//    public Contact getContact(UUID id) {
//        for (Contact c : contacts) {
//            if (c.getId().equals(id))
//                return c;
//        }
//        return null;
//    }
//    
//    public void addContact(Contact contact) {
//        contacts.add(contact);
//    }
//
//    public ArrayList<Contact> getContacts() {
//        return contacts;
//    }
//
//    public void deleteContact(Contact contact) {
//        contacts.remove(contact);
//    }
//
//    public void sortContacts(int mode){
//    	Collections.sort(this.contacts, new ContactComparator(mode));
//    }
//    
//    public void SaveContacts() {
//    	try{
//    		if(this.contacts != null && !this.contacts.isEmpty()) {
//				JSONArray jsonArray = new JSONArray();
//				for(Contact c : this.contacts){
//					jsonArray.put(c.toJSON());
//				}
//				
//				Writer writer = null;
//				try{
//					OutputStream out = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//					writer = new OutputStreamWriter(out);
//					writer.write(jsonArray.toString());
//				}
//				finally{
//					if(writer != null)
//						writer.close();
//				}
//    		}
//		}
//    	catch(Exception ex){
//    		Log.e(TAG, "Error saving contacts:", ex);
//    	}
//	}
//
//	public void LoadContacts() {
//		try{
//			this.contacts.clear();
//			BufferedReader reader = null;
//			
//			try{
//				InputStream in = this.context.openFileInput(FILENAME);
//				reader = new BufferedReader(new InputStreamReader(in));
//				StringBuilder jsonString = new StringBuilder();
//				String line = null;
//				while((line = reader.readLine()) != null){
//					jsonString.append(line);
//				}
//				
//				JSONArray jsonArray = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
//				for(int i = 0; i < jsonArray.length(); i++){
//					this.contacts.add(new Contact(jsonArray.getJSONObject(i)));
//				}
//			}
//			catch(FileNotFoundException e){}
//			finally{
//				if(reader != null)
//					reader.close();
//			}
//		}
//		catch(Exception ex){
//			Log.e(TAG, "Error loading contacts:", ex);
//		}
//	}
//}
