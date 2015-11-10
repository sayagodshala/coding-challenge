package com.sayagodshala.livesplash.network;

/**
 * Created by sayagodshala on 8/8/2015.
 */
public class APIResponse<T> {

    private T feed;

    public APIResponse() {

    }

    public T getFeed() {
        return feed;
    }

    public void setFeed(T feed) {
        this.feed = feed;
    }


}
