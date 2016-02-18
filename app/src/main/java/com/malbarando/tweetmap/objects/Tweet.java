package com.malbarando.tweetmap.objects;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class Tweet {
    public double lat;
    public double lng;
    public String imageUrl;
    public String messageText;
    public String screenName;
    public String jsDump;

    public Tweet(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }


}
