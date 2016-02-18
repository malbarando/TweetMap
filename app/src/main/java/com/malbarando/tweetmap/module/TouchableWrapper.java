package com.malbarando.tweetmap.module;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private OnTouchListener onTouchListener;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchListener.onTouch();
                break;
            case MotionEvent.ACTION_UP:
                onTouchListener.onRelease();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchListener {
        public void onTouch();
        public void onRelease();
    }
}
