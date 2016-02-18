package com.malbarando.tweetmap.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.malbarando.tweetmap.objects.LatLong;
import com.malbarando.tweetmap.objects.MapBounds;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.Constants;
import com.malbarando.tweetmap.utils.CustomRequest;
import com.malbarando.tweetmap.utils.LogUtil;
import com.malbarando.tweetmap.utils.RequestQueueSingleton;

import org.json.JSONObject;

/**
 * Created by Maica Albarando on 2/17/2016.
 */
public class TweetMapRequestHelper {
    private static final String TAG = "RequestHelper";
    private final Context mContext;
    private final Gson gson;

    public TweetMapRequestHelper(Context context) {
        this.mContext = context;
        this.gson = new Gson();

    }

    public void changeLocation(LatLong sw, LatLong ne) {
        String queryUrl = Constants.API_CHANGE_LOCATION;
        MapBounds bounds = new MapBounds(sw, ne);
        JSONObject params = bounds.toJsonParams();
        CustomRequest request = new CustomRequest(Request.Method.POST, queryUrl, params.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtil.printObjectValues(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(request);
    }

    /**
     * Parses JSON to Tweet object
     * @param jsonDump
     * @return
     */
    public Tweet parseTweet(String jsonDump) {
        JsonParser parser = new JsonParser();
        JsonObject jsTweet = parser.parse(jsonDump.toString()).getAsJsonObject();
        JsonArray jsCoordinates = jsTweet.getAsJsonObject("place").getAsJsonObject("bounding_box").getAsJsonArray("coordinates");
        JsonArray jsBounds = jsCoordinates.get(0).getAsJsonArray().get(0).getAsJsonArray();
        JsonObject jsUser = jsTweet.getAsJsonObject("user");
        Tweet tweet = new Tweet(jsBounds.get(1).getAsDouble(), jsBounds.get(0).getAsDouble());
        tweet.id = jsTweet.get("id_str").getAsString();
        tweet.messageText = jsTweet.get("text").getAsString();
        tweet.timeStamp = jsTweet.get("created_at").getAsString();
        tweet.imageUrl = jsUser.get("profile_image_url").getAsString();
        tweet.userId = jsUser.get("screen_name").getAsString();
        tweet.profileName = jsUser.get("name").getAsString();
        tweet.jsDump = gson.toJson(tweet);
        return tweet;
    }
}
