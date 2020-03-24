package si.uni_lj.fri.pbd.contacts;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.LinkedList;
import java.util.List;

public class ContactsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContacts();
    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void getContacts() {
        // from https://stackoverflow.com/questions/26804387/android-fetch-all-contact-list-name-email-phone-takes-more-then-a-minute-for/26820544
        List<Contact> list = new LinkedList<Contact>();
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
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

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
                list.add(contact);
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
                // mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                if (contact.getContactPhoneNumber() == null) {
                    contact.setContactPhoneNumber(data);
                }
            }
        }
        cursor.close();


        for (Contact c : list) {
            Log.d("LIST", c.toString());
        }

    }
}
