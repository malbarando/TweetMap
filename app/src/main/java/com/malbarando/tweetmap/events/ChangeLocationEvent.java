package com.malbarando.tweetmap.events;

import com.malbarando.tweetmap.objects.LatLong;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class ChangeLocationEvent {

    public LatLong northEast;
    public LatLong southWest;

    public ChangeLocationEvent(LatLong northEast, LatLong southWest) {
        this.northEast = northEast;
        this.southWest = southWest;
    }
}
