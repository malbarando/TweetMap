package com.malbarando.tweetmap.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private final TweetRequestListener mListener;
    private final Context mContext;
    private final Gson gson;

    public TweetMapRequestHelper(Context context, TweetRequestListener listener) {
        this.mContext = context;
        this.mListener = listener;

        this.gson = new Gson();

    }

    public interface TweetRequestListener {
        void onConnect(String socketUrl);
    }

    public void changeLocation(LatLong sw, LatLong ne) {
        String queryUrl = Constants.API_CHANGE_LOCATION;
        MapBounds bounds = new MapBounds(sw, ne);
        JSONObject params = bounds.toJsonParams();
        CustomRequest request = new CustomRequest(Request.Method.POST, queryUrl, params.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtil.d("SUCCESS");
                        LogUtil.printObjectValues(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        LogUtil.printObjectValues("error");

                    }
                });

        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public Tweet parseTweet(String jsonDump) {
        JsonParser parser = new JsonParser();
        JsonObject jsTweet = parser.parse(jsonDump.toString()).getAsJsonObject();
        JsonArray jsCoordinates = jsTweet.getAsJsonObject("place").getAsJsonObject("bounding_box").getAsJsonArray("coordinates");
        JsonArray jsBounds = jsCoordinates.get(0).getAsJsonArray().get(0).getAsJsonArray();
        JsonObject jsUser = jsTweet.getAsJsonObject("user");
        Tweet tweet = new Tweet(jsBounds.get(1).getAsDouble(), jsBounds.get(0).getAsDouble());
        tweet.messageText = jsTweet.get("text").getAsString();
        tweet.imageUrl = jsUser.get("profile_image_url").getAsString();
        tweet.screenName = jsUser.get("screen_name").getAsString();
        tweet.jsDump = gson.toJson(tweet);
        return tweet;
    }
}
