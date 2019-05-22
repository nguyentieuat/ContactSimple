package com.example.contactsimple;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.ContactAdapter;
import com.example.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    AutoCompleteTextView txtPhone;
    EditText txtSms;
    Button btnDial, btnCall, btnMessage;

    List listContact;
    ContactAdapter contactAdapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvent();
    }

    private void addEvent() {
        View.OnClickListener event = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = v.getId();
                if (value == R.id.btnDial){
                    xulyDial();
                }
                else if (value == R.id.btnCall){

                    xulyCall();
                }
                else if (value == R.id.btnMessage){
                    xulyMessage();
                }
            }
        };
        btnDial.setOnClickListener(event);
        btnCall.setOnClickListener(event);
        btnMessage.setOnClickListener(event);
    }

    private void xulyMessage() {
    }

    private void xulyCall() {
        Uri uri = Uri.parse("tel:" + txtPhone.getText().toString());
        Intent intent = new Intent(Intent.ACTION_CALL);

    }

    private void xulyDial() {
        Uri uri = Uri.parse("tel:" + txtPhone.getText().toString());
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);
        startActivity(intent);
    }

    private void addControls() {
        txtPhone = findViewById(R.id.txtPhone);
        txtSms = findViewById(R.id.txtMessage);
        btnDial = findViewById(R.id.btnDial);
        btnCall = findViewById(R.id.btnCall);
        btnMessage = findViewById(R.id.btnMessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            listContact = getAllContacts();
//
//        Toast.makeText(MainActivity.this," " + listContact.size(), Toast.LENGTH_LONG).show();
            contactAdapter = new ContactAdapter(MainActivity.this, listContact);
            txtPhone.setAdapter(contactAdapter);
//
            lv = findViewById(R.id.lv);
            lv.setAdapter(contactAdapter);
        }


    }








    private List<Contact> getAllContacts() {
        List<Contact> listContact = new ArrayList<>();

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        String lastPhoneName = " ";
        if (phones.getCount() > 0) {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                String photoUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (!name.equalsIgnoreCase(lastPhoneName)) {
                    lastPhoneName = name;
                    Contact user = new Contact();
                    user.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    user.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[()\\s-]+", ""));
                    listContact.add(user);
                    Log.d("getContactsList", name + "---" + phoneNumber + " -- " + contactId + " -- " + photoUri);
                }
            }
        }
        phones.close();
        return listContact;
    }
}
