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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private ArrayList<Contact> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView contactsList = (ListView) view.findViewById(R.id.contacts_list);

        MainActivity activity = (MainActivity) getActivity();
        list = activity.getContactList();

        ContactAdapter adapter = new ContactAdapter(getContext(), list);
        ListView listView = (ListView) view.findViewById(R.id.contacts_list);
        listView.setAdapter(adapter);
        // try this https://stackoverflow.com/questions/3630468/android-custom-listview-setonitemselectedlistener-not-working
        // xml based on https://stackoverflow.com/questions/5939392/android-checkable-listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.contact_chackbox);
                if (!checkBox.isChecked()) {
                    list.get((int) id).setContactState(true);
                    checkBox.setChecked(true);
                } else {
                    list.get((int) id).setContactState(false);
                    checkBox.setChecked(false);
                }
                String name = ((TextView)view.findViewById(R.id.contact_name)).getText().toString();
                String actualName = list.get((int) id).getContactName();
                Log.d("ITEM SELECTED", name + " " + actualName);
            }
        });

        return view;
    }
}
