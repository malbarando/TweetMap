package com.malbarando.tweetmap.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.malbarando.tweetmap.R;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.RequestQueueSingleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class TweetListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Tweet> mData;
    private ViewHolder viewHolder;
    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public TweetListAdapter(Context mContext, List<Tweet> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.spinner_48_inner_holo)
                .showImageForEmptyUri(R.drawable.img_twitter)
                .showImageOnFail(R.drawable.img_twitter)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_tweet_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.itw_name);
            viewHolder.tvHandler = (TextView) view.findViewById(R.id.itw_handler);
            viewHolder.tvMessage = (TextView) view.findViewById(R.id.itw_message);
            viewHolder.tvTimeStamp = (TextView) view.findViewById(R.id.itw_time);
            viewHolder.imgProfile = (ImageView) view.findViewById(R.id.itw_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Tweet item = mData.get(i);
        if (item != null) {
            viewHolder.tvName.setText(item.profileName);
            viewHolder.tvMessage.setText(item.messageText);
            viewHolder.tvTimeStamp.setText(item.getRelativeTime(item.timeStamp));
            viewHolder.tvHandler.setText(item.getHandler());
            viewHolder.imgProfile.setImageResource(R.drawable.img_twitter);

            ImageLoader.getInstance().displayImage(item.imageUrl, viewHolder.imgProfile, options, animateFirstListener);
        }

        return view;
    }


    static class ViewHolder {
        TextView tvName;
        TextView tvHandler;
        TextView tvMessage;
        TextView tvTimeStamp;
        ImageView imgProfile;
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


}
