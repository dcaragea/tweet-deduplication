package com.shekhargulati.deduplication.tweet.functions;

import com.shekhargulati.deduplication.tweet.Tweet;
import rx.Observable;
import twitter4j.Status;

public class RemoveRetweetTextTransformation implements DeduplicateFunction {

    @Override
    public Observable<Status> apply(Observable<Status> obs) {
        return obs.map(status -> {
            if (status.isRetweet() || status.getText().toLowerCase().startsWith("rt @")) {
                return new Tweet(status, status.getText().replaceFirst("[R|r][T|t] @[A-Za-z0-9]+:", "").trim());
            }
            return status;
        });
    }
}
