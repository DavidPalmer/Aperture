package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.adapter.ContactAdapter;
import com.rewyndr.reflectbig.model.Contacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ContactActivity extends Activity {

    static List<Contacts> allContacts = new ArrayList<Contacts>();
    ContactAdapter cadapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        fetch();
        setTitle("Add attendees");
        cadapter = new ContactAdapter(this, allContacts);
        ListView view = new ListView(this);
        view.setAdapter(cadapter);
        setContentView(view);
    }

    public void fetch() {
        if(allContacts.isEmpty()) {
            HashSet<String> emlRecsHS = new HashSet<String>();
            Context context = this;
            ContentResolver cr = context.getContentResolver();
            String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
            String order = "CASE WHEN " + ContactsContract.Contacts.DISPLAY_NAME
                    + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                    + ContactsContract.Contacts.DISPLAY_NAME + ", "
                    + ContactsContract.CommonDataKinds.Email.DATA
                    + " COLLATE NOCASE";
            String filter = ContactsContract.CommonDataKinds.Email.DATA
                    + " NOT LIKE ''";
            Cursor cur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
                    filter, null, order);
            if (cur.moveToFirst()) {
                do {
                    // names comes in hand sometimes
                    String id = cur.getString(0);
                    String name = cur.getString(1);
                    String emlAddr = cur.getString(3);
                    Contacts contact = new Contacts(name, emlAddr, id);
                    if (emlRecsHS.add(emlAddr.toLowerCase())) {
                        allContacts.add(contact);
                    }
                } while (cur.moveToNext());
            }
            cur.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addContact) {
            Intent data = new Intent();
            data.putExtra("myobj", cadapter.getSelected());
            setResult(Activity.RESULT_OK, data);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}