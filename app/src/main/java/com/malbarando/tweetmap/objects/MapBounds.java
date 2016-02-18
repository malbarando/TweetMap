package com.malbarando.tweetmap.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class MapBounds {
    public LatLong sw;
    public LatLong ne;

    public static final String SW = "sw";
    public static final String NE = "ne";

    public MapBounds(LatLong sw, LatLong ne) {
        this.sw = sw;
        this.ne = ne;
    }

    public JSONObject toJsonParams() {
        JSONObject params = new JSONObject();
        try {
            params.put(SW, sw.toJson());
            params.put(NE, ne.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }
}
