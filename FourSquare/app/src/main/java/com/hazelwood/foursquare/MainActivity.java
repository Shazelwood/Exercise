package com.hazelwood.foursquare;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements FragmentList.ListFragmentListener {
    EditText locationName;
    Button searchBtn;
    ImageButton currentLocationBtn;
    LocationManager locManager;
    FragmentManager fragManager;
    Location location;
    double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragManager = getFragmentManager();


        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


        locationName = (EditText) findViewById(R.id.etLocation);
        searchBtn = (Button) findViewById(R.id.btnSearch);
        currentLocationBtn = (ImageButton) findViewById(R.id.btnCurrentLocation);

        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    showSettingsAlert();
                } else {
                    locationName.setText(longitude + ", " + latitude);
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationName.getText().length() > 0){
                    String locName = locationName.getText().toString();
                    Task task = new Task();
                    task.execute(locName);
                }
            }
        });

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public class Task extends AsyncTask<String, Void, ArrayList<Venue>>{
        ProgressDialog dialog;
        String jsonString, name, phoneNumber, address;;
        ArrayList<Venue> arrayList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle((ProgressDialog.STYLE_HORIZONTAL));
            dialog.setIndeterminate(true);
            dialog.setProgressNumberFormat("Please wait..");
            dialog.setProgressPercentFormat(null);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<Venue> doInBackground(String... params) {

            try {
                String urlString = "https://api.foursquare.com/v2/venues/search?" +
                        "client_id=5Y3J3Y03PDID3CMGUPJRTTU2KNPVRGFRZSUZU4UKSJ0TYINI&" +
                        "client_secret=CF0E14Y1NTF22Z1ZJQLT5XLTUPXUZFLFHZNZVIZSNBXGNVCY&limit=10&" +
                        "v=20130815%20&near=" + URLEncoder.encode(params[0], "UTF-8");

                URL url = new URL(urlString);
                arrayList = new ArrayList<Venue>();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                jsonString = IOUtils.toString(is);
                is.close();
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //JSON

                JSONObject outerObject = new JSONObject(jsonString);
                JSONObject response = outerObject.getJSONObject("response");
                JSONArray venues = response.getJSONArray("venues");

                for (int i = 0; i < venues.length(); i++){
                    JSONObject object = venues.getJSONObject(i);
                    if (object.has("name")){
                        name = object.getString("name");
                    }
                    if (object.has("contact")){
                        JSONObject contact = object.getJSONObject("contact");
                        if (contact.has("phone")) phoneNumber = contact.getString("phone");
                    }
                    if (object.has("location")){
                        JSONObject location = object.getJSONObject("location");
                        if (location.has("address")) address = location.getString("address");
                    }
                    arrayList.add(new Venue(name, phoneNumber, address));
                }





            } catch (JSONException e) {
                e.printStackTrace();

            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Venue> strings) {
            super.onPostExecute(strings);
            dialog.dismiss();



            fragManager.beginTransaction().replace(R.id.container, FragmentList.newInstance(strings), "Fragment List").commit();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Location Service");
        alertDialog.setMessage("Turn on location OR cancel and Enter location");
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
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

    @Override
    public void getURL(String url) {

    }
}
