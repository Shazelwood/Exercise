package com.hazelwood.foursquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 8/18/15.
 */
public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Venue> mObjects;

    private static final long ID_CONSTANT = 123456789;

    public ListAdapter(Context c, ArrayList<Venue> objects){
        mContext = c;
        mObjects = objects;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Venue venue = (Venue) getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_item, parent, false);
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.fragment_item_name);
        nameView.setText(venue.getName());

        TextView phoneView = (TextView) convertView.findViewById(R.id.fragment_item_number);

        if (venue.getContactNumber() == null){
            phoneView.setText("N/A");
        } else {
            phoneView.setText("Phone: " + venue.getContactNumber());
        }

        return convertView;
    }
}
