package com.meedbankingclub.cordova.contacts;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;


public class ContactSdk {

    private String LOG_TAG = "MEED_CONTACTS_PLUGIN";

    private CordovaInterface cordova;

    public ContactSdk(CordovaInterface cordova) {
        this.cordova = cordova;
    }


    public JSONArray all(JSONObject options) {

        JSONArray contacts;

        int limit = options.optInt("limit", Integer.MAX_VALUE);
        int offset = options.optInt("skip", 0);

        ContentResolver cr = this.cordova.getActivity().getContentResolver();
        String[] PROJECTION = new String[]{
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
        };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE "
                + "LIMIT " + limit
                + " OFFSET " + offset;

        String selection = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                PROJECTION, selection, null, order);


        contacts = filterEmails(cursor);

        cursor.close();

        return contacts;
    }






    public JSONArray search(String searchString, JSONObject options) {

        JSONArray contacts;

        int limit = options.optInt("limit", Integer.MAX_VALUE);
        int offset = options.optInt("skip", 0);


        String[] PROJECTION = new String[]{
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
        };

        String SELECTION =
                Contacts.DISPLAY_NAME + " LIKE ? AND "
                + Email.ADDRESS + " NOT LIKE ''";

        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE "
                + "LIMIT " + limit
                + " OFFSET " + offset;


        Cursor cursor = this.cordova.getActivity().getContentResolver().query(
                Email.CONTENT_URI,
                PROJECTION,
                SELECTION,
                new String[]{ "%" + searchString + "%" },
                order
        );

        contacts = this.filterEmails(cursor);

        cursor.close();


        return contacts;
    }


    JSONArray filterEmails(Cursor cursor) {
        HashSet<String> unique = new HashSet<String>();
        JSONArray contacts = new JSONArray();

        int index = -1;
        if (cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                try {
                    String name = cursor.getString(1);
                    String email = cursor.getString(2);

                    if (unique.add(name.toLowerCase())) {
                        contacts.put(new JSONObject()
                                .put("name", name)
                                .put("emails", new JSONArray().put(email))
                        );
                        index++;
                    } else {
                        JSONObject contact = contacts.getJSONObject(index);
                        JSONArray emails = contact.getJSONArray("emails");
                        emails.put(email);
                        contacts.put(index, new JSONObject()
                                .put("name", name)
                                .put("emails", emails));
                    }
                } catch (JSONException e) {
                    LOG.d(LOG_TAG, e.getMessage());
                }
            }
        }
        return contacts;

    }


}