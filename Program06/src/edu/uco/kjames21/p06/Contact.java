package edu.uco.kjames21.p06;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;


public class Contact {
	private static final String JSON_ID = "id";
	private static final String JSON_FIRSTNAME = "firstName";
    private static final String JSON_LASTNAME = "lastName";
    private static final String JSON_AGE = "age";
    private static final String JSON_PHONE = "phone";
    private static final String JSON_EMAIL = "email";
    private static final String JSON_PHOTOFILENAME = "photo";
	
    private UUID id;
	private String firstName;
    private String lastName;
    private Integer age;
    private String phone;
    private String email;
    private String photo;

    public Contact() {
        this.id = UUID.randomUUID();
    }
    
    public Contact(JSONObject json) throws JSONException {
    	this.id = UUID.fromString(json.getString(JSON_ID));
    	this.firstName = json.getString(JSON_FIRSTNAME);
    	this.lastName = json.getString(JSON_LASTNAME);
    	
    	String ageString = json.getString(JSON_AGE);
    	this.age = ageString == null || ageString.equals("") ? null : Integer.parseInt(ageString);
    	
    	this.phone = json.getString(JSON_PHONE);
    	this.email = json.getString(JSON_EMAIL);
    	this.photo = json.getString(JSON_PHOTOFILENAME);
    }
    
    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoto(){
		return this.photo;
	}
	
	public void setPhoto(String photo){
		this.photo = photo;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject result = new JSONObject();
		
		result.put(JSON_ID, this.id);
		result.put(JSON_FIRSTNAME, this.firstName);
		result.put(JSON_LASTNAME, this.lastName);
		result.put(JSON_AGE, this.age);
		result.put(JSON_PHONE, this.phone);
		result.put(JSON_EMAIL, this.email);
		result.put(JSON_PHOTOFILENAME, this.photo);
		
		return result;
	}
}