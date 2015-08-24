package com.hazelwood.foursquare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;


public class FragmentList extends Fragment {
    private static final String ARG = "FOURSQUARE";
    private Serializable mParam;
    ListView listView;
    Task task;

    public FragmentList() {}
    private ListFragmentListener mListener;

    public interface ListFragmentListener {
        public void getURL(String url);
    }

    public static FragmentList newInstance(String location) {
        FragmentList fragment = new FragmentList();
        Bundle args = new Bundle();
        args.putString(ARG, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        String location = args.getString(ARG);
        runTask(location);

        listView = (ListView) getActivity().findViewById(R.id.list_view);
    }

    public void runTask(String location){
        stopTask();
        task = new Task();
        task.execute(location);
    }

    public void stopTask(){
        if (task != null){
            task.cancel(false);
            task = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        stopTask();
    }

    public class Task extends AsyncTask<String, Void, ArrayList<Venue>> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle((ProgressDialog.STYLE_HORIZONTAL));
            dialog.setIndeterminate(true);
            dialog.setProgressNumberFormat("Getting Venues..");
            dialog.setProgressPercentFormat(null);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<Venue> doInBackground(String... params) {

            ArrayList<Venue> venues = null;

            venues = ListHelper.getVenues(params[0]);

            return venues;

        }

        @Override
        protected void onPostExecute(final ArrayList<Venue> strings) {
            super.onPostExecute(strings);
            dialog.dismiss();

            task = null;

            ListAdapter listAdapter = new ListAdapter(getActivity(), strings);

            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Venue venue = strings.get(position);
                    itemChoiceDialog(venue.getName(), venue.getContactNumber(), venue.getAddress());

                }
            });

        }
    }

    public void itemChoiceDialog(String name, final String number, final String address) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(name);
        alertDialog.setMessage("What would you like to do?");
        alertDialog.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
        alertDialog.setNeutralButton("Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String uri = "geo:0,0?q=" + address;
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
