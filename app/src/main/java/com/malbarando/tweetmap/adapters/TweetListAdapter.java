package com.malbarando.tweetmap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malbarando.tweetmap.R;
import com.malbarando.tweetmap.objects.Tweet;

import java.util.List;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class TweetListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Tweet> mData;
    private ViewHolder viewHolder;

    public TweetListAdapter(Context mContext, List<Tweet> mData) {
        this.mContext = mContext;
        this.mData = mData;
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
}
