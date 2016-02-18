package com.malbarando.tweetmap.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.malbarando.tweetmap.R;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.LogUtil;
import com.malbarando.tweetmap.utils.RequestQueueSingleton;


/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class TweetInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final LayoutInflater inflater;
    private Gson gson;
    private View popup;
    private final Context context;

    public TweetInfoWindowAdapter(Context context, LayoutInflater inflater) {
        this.inflater = inflater;
        this.context = context;
        gson = new Gson();

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = inflater.inflate(R.layout.tweet_info_window, null);
        }

        final ImageView mImageView = ((ImageView) popup.findViewById(R.id.tw_image));
        String jsDump = marker.getSnippet();
        Tweet tweet = gson.fromJson(jsDump, Tweet.class);

        TextView tvTitle = ((TextView) popup.findViewById(R.id.tw_name));
        tvTitle.setText(tweet.screenName + ": " + tweet.messageText);

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(tweet.imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        // set default image
                        mImageView.setImageResource(R.drawable.img_twitter);
                    }
                });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
        return popup;
    }
}
