package com.malbarando.tweetmap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.malbarando.tweetmap.base.BaseEventActivity;
import com.malbarando.tweetmap.events.AddTweetEvent;
import com.malbarando.tweetmap.events.ChangeLocationEvent;
import com.malbarando.tweetmap.events.ClearListEvent;
import com.malbarando.tweetmap.events.ShowDialogEvent;
import com.malbarando.tweetmap.fragments.MapFragment;
import com.malbarando.tweetmap.helpers.TweetMapRequestHelper;
import com.malbarando.tweetmap.module.MapStateListener;
import com.malbarando.tweetmap.module.TouchableMapFragment;
import com.malbarando.tweetmap.objects.LatLong;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.Constants;
import com.malbarando.tweetmap.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.net.MalformedURLException;

import de.greenrobot.event.EventBus;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;


public class MainActivity extends BaseEventActivity {
    private TweetMapRequestHelper mHelper;
    private GoogleMap map;
    private SocketIO socket;

    public void onEvent(ChangeLocationEvent e) {
        changeLoc();
    }

    public void onEventMainThread(ShowDialogEvent e) {
        launchReconnectDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TweetMapRequestHelper(this);
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();
        ImageLoader.getInstance().init(config);

        soConnect();
        TouchableMapFragment touchableMapFragment = (TouchableMapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = touchableMapFragment.getMap();
        new MapStateListener(map, touchableMapFragment, this) {
            @Override
            public void onMapTouched() {
                // Map touched
            }

            @Override
            public void onMapReleased() {
                // Map released
                changeLoc();
                LogUtil.w("onMapReleased");

            }

            @Override
            public void onMapUnsettled() {
                // Map unsettled
            }

            @Override
            public void onMapSettled() {
                // Map settled
                LogUtil.w("onMapSettled");
//                changeLoc();
            }
        };

    }

    private void changeLoc() {
        if (map == null) return;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mHelper.changeLocation(new LatLong(bounds.southwest.latitude, bounds.southwest.longitude),
                new LatLong(bounds.northeast.latitude, bounds.northeast.longitude));


        if (!socket.isConnected()) {
            launchReconnectDialog();
        }
    }

    private void clearList() {
        EventBus.getDefault().post(new ClearListEvent());
        if (map != null) {
            map.clear();
        }
    }

    private void soConnect() {
        try {
            socket = new SocketIO(Constants.SOCKET_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {

            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                LogUtil.d("an Error occured");
                socketIOException.printStackTrace();
                EventBus.getDefault().post(new ShowDialogEvent());
            }

            @Override
            public void onDisconnect() {
                LogUtil.d("Connection terminated.");
            }

            @Override
            public void onConnect() {
                LogUtil.d("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                if (event.equals("tweet")) {
                    MapFragment touchableMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                    Tweet newTweet = mHelper.parseTweet(args[0].toString());
                    touchableMapFragment.createMarker(newTweet);
                    EventBus.getDefault().post(new AddTweetEvent(newTweet));
                }
            }
        });
    }

    private void launchReconnectDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.error_title));
        builder.setMessage(getString(R.string.error_message));
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                soConnect();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            changeLoc();
            clearList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
