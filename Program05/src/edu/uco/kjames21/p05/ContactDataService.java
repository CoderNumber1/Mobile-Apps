package edu.uco.kjames21.p05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import android.content.Context;

public class ContactDataService {
    private ArrayList<Contact> contacts;

    private static ContactDataService dataService;
    //private Context mAppContext;

    private ContactDataService(Context appContext) {
        //mAppContext = appContext;
        contacts = new ArrayList<Contact>();
    }

    public static ContactDataService get(Context context) {
        if (dataService == null) {
            dataService = new ContactDataService(context.getApplicationContext());
            
          //Create some initial contacts
            Contact c1 = new Contact();
            c1.setFirstName("Anthony");
            c1.setLastName("James");
            c1.setAge(24);
            c1.setPhone("405-555-5555");
            c1.setEmail("kjames21@uco.edu");
            dataService.addContact(c1);
            
            Contact c2 = new Contact();
            c2.setFirstName("Cool");
            c2.setLastName("Guy");
            c2.setAge(30);
            c2.setPhone("405-123-4567");
            c2.setEmail("cool@uco.edu");
            dataService.addContact(c2);
            
            Contact c3 = new Contact();
            c3.setFirstName("Sweet");
            c3.setLastName("Girl");
            c3.setAge(19);
            c3.setPhone("405-765-3214");
            c3.setEmail("sg12@uco.edu");
            dataService.addContact(c3);
            
            Contact c4 = new Contact();
            c4.setFirstName("Betty");
            c4.setLastName("White");
            c4.setAge(104);
            c4.setPhone("1800-768-7654");
            c4.setEmail("betty.white@celeb.com");
            dataService.addContact(c4);
            
            Contact c5 = new Contact();
            c5.setFirstName("Little");
            c5.setLastName("Kid");
            c5.setAge(5);
            c5.setPhone("654-321-9876");
            c5.setEmail("little.kid@kids.edu");
            dataService.addContact(c5);
        }
        return dataService;
    }

    public Contact getContact(UUID id) {
        for (Contact c : contacts) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    public void sortContacts(int mode){
    	Collections.sort(this.contacts, new ContactComparator(mode));
    }
}
