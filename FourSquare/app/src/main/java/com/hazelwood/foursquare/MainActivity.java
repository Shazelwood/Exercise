package com.hazelwood.foursquare;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


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
                    fragManager.beginTransaction()
                            .replace(R.id.container, FragmentList.newInstance(locName), "Fragment List")
                            .commit();

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
