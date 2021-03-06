package si.uni_lj.fri.pbd.contacts;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

// based on https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView

public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(@NonNull Context context, ArrayList<Contact> contactList) {
        super(context, 0, contactList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }
        // Lookup view for data population
        TextView contactName = (TextView) convertView.findViewById(R.id.contact_name);
        // Populate the data into the template view using the data object
        contactName.setText(contact.getContactName());
        // We need to handle the state ob checkbox here because of the recycling nature of this View
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.contact_chackbox);
        checkBox.setChecked(contact.getContactState());
        // Return the completed view to render on screen
        return convertView;
    }
}
