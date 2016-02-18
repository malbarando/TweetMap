package com.malbarando.tweetmap.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class LatLong {
    public double latitude;
    public double longitude;

    public static final String LNG = "lng";
    public static final String LAT = "lat";

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public JSONObject toJson() {
        JSONObject params = new JSONObject();
        try {
            params.put(LNG, longitude);
            params.put(LAT, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;

    }

}
