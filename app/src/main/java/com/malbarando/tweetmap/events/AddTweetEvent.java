package com.malbarando.tweetmap.events;

import com.malbarando.tweetmap.objects.Tweet;

/**
 * Created by Maica Albarando on 2/18/2016.
 */
public class AddTweetEvent {
    public Tweet tweet;

    public AddTweetEvent(Tweet tweet) {
        this.tweet = tweet;
    }
}
