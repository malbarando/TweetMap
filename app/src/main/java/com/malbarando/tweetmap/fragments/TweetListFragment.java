package com.malbarando.tweetmap.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.malbarando.tweetmap.R;
import com.malbarando.tweetmap.adapters.TweetListAdapter;
import com.malbarando.tweetmap.events.AddTweetEvent;
import com.malbarando.tweetmap.events.ClearListEvent;
import com.malbarando.tweetmap.objects.Tweet;
import com.malbarando.tweetmap.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class TweetListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<Tweet> mData;
    private List<Tweet> mTmpData;
    private TweetListAdapter mAdapter;
    private ListView mListView;
    private Button mBtnRefresh;

    public void onEventMainThread(AddTweetEvent e) {
        addTweet(e.tweet);
    }

    public void onEventMainThread(ClearListEvent e) {
        clearList();
    }

    private void clearList() {
        clearData();
        mAdapter.notifyDataSetChanged();
        mBtnRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();
        mTmpData = new ArrayList<>();
        mAdapter = new TweetListAdapter(getActivity(), mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        mListView = (ListView) view.findViewById(R.id.lvTweets);
        TextView mEmptyView = (TextView) view.findViewById(R.id.emptyElement);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(this);

        mBtnRefresh = (Button) view.findViewById(R.id.btnNewTweets);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnRefresh.setVisibility(View.GONE);
                mData.clear();
                mData.addAll(mTmpData);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addTweet(Tweet t) {
        mTmpData.add(0, t);
        if (mData.size() < 10) {
            mData.clear();
            mData.addAll(mTmpData);
            mAdapter.notifyDataSetChanged();
            mBtnRefresh.setVisibility(View.GONE);
        } else {
            mBtnRefresh.setVisibility(View.VISIBLE);
        }
    }

    public void clearData() {
        mData.clear();
        mTmpData.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Tweet tweet = mData.get(i);
        if (tweet != null) {
            launchTwitterApp(tweet.id);
        }
    }

    public void launchTwitterApp(String tweetId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://status?status_id=" + tweetId));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
