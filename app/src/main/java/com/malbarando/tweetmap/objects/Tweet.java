package com.malbarando.tweetmap.objects;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class Tweet {
    public double lat;
    public double lng;
    public String userId;
    public String profileName;
    public String imageUrl;
    public String messageText;
    public String jsDump;
    public String timeStamp;
    public String id;

    public Tweet(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public String getHandler() {
        return "@" + userId;
    }


    public static String getRelativeTime(String timeStamp) {
        return  DateUtils.getRelativeTimeSpanString(new Date().getTime(), new Date(timeStamp).getTime(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_NO_NOON).toString();
    }
}
