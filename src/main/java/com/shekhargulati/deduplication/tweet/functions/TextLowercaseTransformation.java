package com.shekhargulati.deduplication.tweet.functions;

import com.shekhargulati.deduplication.tweet.Tweet;
import rx.Observable;
import twitter4j.Status;

public class TextLowercaseTransformation implements DeduplicateFunction {

    @Override
    public Observable<Status> apply(Observable<Status> obs) {
        return obs.map(s -> new Tweet(s, s.getText().toLowerCase()));
    }
}
