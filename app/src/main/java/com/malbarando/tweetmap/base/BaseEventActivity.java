package com.malbarando.tweetmap.base;

import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;


/**
 * Created by Maica Albarando on 2/18/2016.
 */
public abstract class BaseEventActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            if (isStickyAvailable()) {
                EventBus.getDefault().registerSticky(this);
            } else {
                EventBus.getDefault().register(this);
            }
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    protected boolean isStickyAvailable() {
        return false;
    }


}
