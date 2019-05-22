package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.contactsimple.MainActivity;
import com.example.contactsimple.R;
import com.example.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    List<Contact> listContact;

    public ContactAdapter(MainActivity context, List<Contact> contactList) {
        super(context, 0, contactList);
        listContact = new ArrayList<>(contactList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_item_contact, parent, false
            );
        }

        TextView txtNameUser = convertView.findViewById(R.id.txtNameUser);
        TextView txtNumPhone = convertView.findViewById(R.id.txtNumPhone);

//        Contact contact = this.objects.get(position);
//        txtNameUser.setText(contact.getName());
//        txtNumPhone.setText(contact.getPhone());

        Contact contact = getItem(position);
        if (contact != null) {
            txtNameUser.setText(contact.getName());
            txtNumPhone.setText(contact.getPhone());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Contact> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(listContact);
                    Log.d("filter", "This is filter /" + listContact.size());

            } else {
                Log.d("filter", "This is filter /" +constraint.length() +" | "+ constraint.toString());
                String filterPattern = constraint.toString().trim();

                for (Contact item : listContact) {
                    if (item.getPhone().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Contact) resultValue).getPhone();
        }
    };
}
