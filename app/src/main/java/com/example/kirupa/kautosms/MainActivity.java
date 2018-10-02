package com.example.kirupa.kautosms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.kirupa.kautosms.App.getAppContext;


public class MainActivity extends AppCompatActivity {

    private int SMS_PERMISSION_CODE = 23;
    private static final int PICK_CONTACT = 1000;
    ImageView img_contact;
    public static RecyclerView recyclerView;
    public static EditText ePhone;
    TextView add;
    public static ArrayList<String> phoneNumbers=new ArrayList<>();
    public static ArrayList<String> phoneNumbers_new=new ArrayList<>();
    public String contactNumber;
    String subString;
//    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        setContext(getApplicationContext());

        String[] getPhoneNumbers = null;

        getPhoneNumbers = new SharedPreference(getAppContext()).getPhoneNumbers();
        if(getPhoneNumbers!=null){
            System.out.println("getPhoneNumbers length:"+getPhoneNumbers.length);
            phoneNumbers.clear();

            for (int i = 0; i < getPhoneNumbers.length; i++) {
                phoneNumbers.add(getPhoneNumbers[i]);

            }


        }else {

            System.out.println("getPhoneNumbers null:");

        }
        refresh();

        ePhone=(EditText)findViewById(R.id.edit_mobile);
        System.out.println("ePhone length is:"+ePhone.length());

        add=(TextView) findViewById(R.id.txt_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ePhone length is:"+ePhone.length());

                int length=ePhone.getText().length();
                System.out.println("ePhone length is1:"+length);

                if(ePhone.getText().toString().equals(null)||ePhone.getText().toString().equals("")||
                        length<10)
                {
                    ePhone.setError("Please Enter Valid Phone Number");
                }else{

                    phoneNumbers.add(ePhone.getText().toString());
                    ePhone.setText("");
                    refresh();

                }
            }
        });
        Log.d("MainActivity","Size::>>"+phoneNumbers.size());
        // call the constructor of UsersAdapter to send the reference and data to Adapter

        img_contact=(ImageView)findViewById(R.id.img_contact);
        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        if (isReadStorageAllowed()) {
            //If permission is already having then showing the toast
            Toast.makeText(MainActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();
    }

    public static void refresh() {
        System.out.println("phonearray size:"+phoneNumbers.size());

        new SharedPreference(getAppContext()).savePhoneNumber(phoneNumbers);


        ArrayListRecyclerviewAdapter recyclerViewAdapter  = new ArrayListRecyclerviewAdapter(getAppContext(), phoneNumbers);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {

        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,Manifest.permission.READ_CONTACTS}, SMS_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == SMS_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor phone = getContentResolver().query(contactData, null, null, null, null);
                    if (phone.moveToFirst()) {
                        String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone =phone.getString(phone.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String id =phone.getString(phone.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            contactNumber = phones.getString(phones.getColumnIndex("data1"));
                            char[] cArray=contactNumber.toCharArray();
                            System.out.println("cArray length is:"+cArray.length);

                            if(cArray.length>10){
                              subString =contactNumber.substring(3);
                              System.out.println("subString is:"+subString);
                               ePhone.setText(subString);
                            }else{
                                System.out.println("number is:"+contactNumber);
                                ePhone.setText(contactNumber);

                            }

                        }

//                        String contactNumber = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                        Toast.makeText(this, "Name:"+contactNumberName+contactNumber, Toast.LENGTH_LONG).show();

                    }
                }
                break;
        }
    }

}