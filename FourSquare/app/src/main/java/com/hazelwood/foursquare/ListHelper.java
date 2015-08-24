package com.hazelwood.foursquare;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Hazelwood on 8/24/15.
 */
public class ListHelper {

    public static ArrayList<Venue> getVenues(String loc) {
        ArrayList<Venue> venues = new ArrayList<>();
        String jsonString = "", name = "", phoneNumber = "", address = "";

        try {
            String urlString = "https://api.foursquare.com/v2/venues/search?" +
                    "client_id=5Y3J3Y03PDID3CMGUPJRTTU2KNPVRGFRZSUZU4UKSJ0TYINI&" +
                    "client_secret=CF0E14Y1NTF22Z1ZJQLT5XLTUPXUZFLFHZNZVIZSNBXGNVCY&limit=10&" +
                    "v=20130815%20&near=" + URLEncoder.encode(loc, "UTF-8");

            URL url = new URL(urlString);
            venues = new ArrayList<Venue>();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            jsonString = IOUtils.toString(is);
            is.close();
            connection.disconnect();

            //JSON

            JSONObject outerObjectJSON = new JSONObject(jsonString);
            JSONObject responseJSON = outerObjectJSON.getJSONObject("response");
            JSONArray venuesJSON = responseJSON.getJSONArray("venues");

            for (int i = 0; i < venuesJSON.length(); i++) {
                JSONObject object = venuesJSON.getJSONObject(i);
                if (object.has("name")) {
                    name = object.getString("name");
                }
                if (object.has("contact")) {
                    JSONObject contact = object.getJSONObject("contact");
                    if (contact.has("phone")) phoneNumber = contact.getString("phone");
                }
                if (object.has("location")) {
                    JSONObject location = object.getJSONObject("location");
                    if (location.has("address")) address = location.getString("address");
                }
                venues.add(new Venue(name, phoneNumber, address));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return venues;
    }
}
