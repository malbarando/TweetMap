package com.malbarando.tweetmap.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malbarando.tweetmap.R;
import com.malbarando.tweetmap.events.ChangeLocationEvent;
import com.malbarando.tweetmap.module.TouchableMapFragment;
import com.malbarando.tweetmap.module.TouchableWrapper;
import com.malbarando.tweetmap.adapters.TweetInfoWindowAdapter;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.Constants;

import de.greenrobot.event.EventBus;


/**
 * Created by Maica Albarando on 2/17/2016.
 */
public class MapFragment extends TouchableMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraChangeListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    // shows generic map
    private int MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        getMap().setInfoWindowAdapter(new TweetInfoWindowAdapter(getActivity(), getActivity().getLayoutInflater()));
    }


    @Override
    public void setTouchListener(TouchableWrapper.OnTouchListener onTouchListener) {
        super.setTouchListener(onTouchListener);
    }

    public void createMarker(final Tweet tweet) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Marker marker = getMap().addMarker(new MarkerOptions()
                        .position(tweet.getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_twitter_pin))
                        .snippet(tweet.jsDump));
                // remove marker after specified time
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                marker.remove();
                            }
                        },
                        Constants.TWEET_EXPIRY);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation == null) {
            setDefaultLocation();
        }
        initMapCamera(mCurrentLocation);
    }

    private void initMapCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(8f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        getMap().animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        getMap().setMapType(MAP_TYPE);
        getMap().setMyLocationEnabled(true);
        EventBus.getDefault().post(new ChangeLocationEvent());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Set default location to Manila
        setDefaultLocation();
        initMapCamera(mCurrentLocation);
    }

    public void setDefaultLocation() {
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(37.422535);
        mCurrentLocation.setLongitude(-122.084804);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }
}
