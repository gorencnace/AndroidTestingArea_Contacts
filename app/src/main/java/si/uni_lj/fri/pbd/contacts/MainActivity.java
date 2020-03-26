package si.uni_lj.fri.pbd.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }
        setContactList();

        setContentView(R.layout.activity_main);
    }

    // from https://www.androidauthority.com/send-sms-messages-app-development-856280/

    public boolean checkPermission() {
        int cpr = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        return cpr == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        Manifest.permission.READ_CONTACTS
                }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean ReadContactsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (ReadContactsPermission) {
                        Toast.makeText(MainActivity.this, "Permission accepted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
        }
    }


    public void setContactList() {
        // from https://stackoverflow.com/questions/26804387/android-fetch-all-contact-list-name-email-phone-takes-more-then-a-minute-for/26820544
        this.contactList = new ArrayList<Contact>();
        LongSparseArray<Contact> array = new LongSparseArray<Contact>();

        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE,
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
        };
        String sortOrder = ContactsContract.Contacts.SORT_KEY_ALTERNATIVE;

        //Uri uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
        Uri uri = ContactsContract.Data.CONTENT_URI;
// we could also use Uri uri = ContactsContract.Data.CONTENT_URI;

// ok, let's work...
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        final int mimeTypeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        final int idIdx = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        final int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int dataIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA);
        final int typeIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idIdx);
            Contact contact = array.get(id);
            if (contact == null) {
                contact = new Contact();
                contact.setContactId(id);
                contact.setContactName(cursor.getString(nameIdx));
                array.put(id, contact);
                contactList.add(contact);
            }
            int type = cursor.getInt(typeIdx);
            Log.d("TYPE", String.valueOf(type));
            String data = cursor.getString(dataIdx);
            Log.d("DATA", data);
            String mimeType = cursor.getString(mimeTypeIdx);
            Log.d("MIME_TYPE", mimeType);
            if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                // mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                if (contact.getContactEmailAddress() == null) {
                    contact.setContactEmailAddress(data);
                }
            } else {
                if (contact.getContactPhoneNumber() == null) {
                    contact.setContactPhoneNumber(data);
                }
            }
        }
        cursor.close();
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }
}
