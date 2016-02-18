package com.malbarando.tweetmap;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.malbarando.tweetmap.base.BaseEventActivity;
import com.malbarando.tweetmap.events.ChangeLocationEvent;
import com.malbarando.tweetmap.fragments.MapFragment;
import com.malbarando.tweetmap.helpers.TweetMapRequestHelper;
import com.malbarando.tweetmap.module.MapStateListener;
import com.malbarando.tweetmap.module.TouchableMapFragment;
import com.malbarando.tweetmap.objects.LatLong;
import com.malbarando.tweetmap.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.logging.Level;

import de.tavendo.autobahn.WebSocketConnection;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

//import io.socket.emitter.Emitter;


public class MainActivity extends BaseEventActivity implements TweetMapRequestHelper.TweetRequestListener{
    private static final String TAG = "tweet-";
    private static final String WS_URL = "ws://ec2-52-73-201-216.compute-1.amazonaws.com/socket.io/1/websocket/957049465133";
//    private static final String URL = "http://52.73.201.216/tweets";
    private static final String URL = "http://ec2-52-73-201-216.compute-1.amazonaws.com/tweets";
    private TweetMapRequestHelper mHelper;


    public void onEvent(ChangeLocationEvent e) {
        LogUtil.d("change location event");
        mHelper.changeLocation(e.southWest, e.northEast);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        soConnect();
        TouchableMapFragment touchableMapFragment = (TouchableMapFragment) getFragmentManager().findFragmentById(R.id.map);
        final GoogleMap map = touchableMapFragment.getMap();
        new MapStateListener(map, touchableMapFragment, this) {
            @Override
            public void onMapTouched() {
                // Map touched
            }

            @Override
            public void onMapReleased() {
                // Map released
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                mHelper.changeLocation(new LatLong(bounds.southwest.latitude, bounds.southwest.longitude),
                        new LatLong(bounds.northeast.latitude, bounds.northeast.longitude)
                        );
                LogUtil.d("map releaseddddddddd");
               /* mHelper.changeLocation(new LatLong(120.93313390000003, 14.5571256),
                        new LatLong(121.0261686, 14.6390175));*/
            }

            @Override
            public void onMapUnsettled() {
                // Map unsettled
            }

            @Override
            public void onMapSettled() {
                // Map settled
                /*LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                mHelper.changeLocation(new LatLong(bounds.southwest.latitude, bounds.southwest.longitude),
                        new LatLong(bounds.northeast.latitude, bounds.northeast.longitude)
                );
                LogUtil.d("map settled!!!!!!!!!!");*/
            }
        };

//        start();
    }



    private void soConnect() {
        SocketIO socket = null;
        try {
            socket = new SocketIO(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    System.out.println("Server said:" + json.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                System.out.println("an Error occured");
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                System.out.println("Server triggered event '" + event + "'");
                if (event.equals("tweet")) {
                    LogUtil.d("new tweet");
                    MapFragment touchableMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                   /* Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonObject tweet = parser.parse(args[0].toString()).getAsJsonObject();
                    LogUtil.printObjectValues(tweet);
                    JsonArray coordinates = tweet.getAsJsonObject("place").getAsJsonObject("bounding_box").getAsJsonArray("coordinates");
                    LogUtil.printObjectValues("------------------------------------------");
                    LogUtil.printObjectValues(coordinates);
                    JsonArray bounds = coordinates.get(0).getAsJsonArray().get(0).getAsJsonArray();
                    LogUtil.printObjectValues(bounds);

//                            .getAsJsonObject("bounding_bounds").getAsJsonArray("coordinates");
//                    JsonArray bounds = coordinates.get(0).getAsJsonArray().get(0).getAsJsonArray();
*/
                    touchableMapFragment.createMarker(mHelper.parseTweet(args[0].toString()));
                }
            }
        });

        // This line is cached until the connection is establisched.
//        socket.send("Hello Server!");
    }

    public void init() {
        mHelper = new TweetMapRequestHelper(this, this);
        // init ImageLoader
        // Create global configuration and initialize ImageLoader with this config
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Connect to the server.
     */

    @Override
    public void onConnect(String socketUrl) {
        Log.i("yes", socketUrl);
    }

}
