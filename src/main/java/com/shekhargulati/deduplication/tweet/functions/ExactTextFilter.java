package com.shekhargulati.deduplication.tweet.functions;

import com.shekhargulati.deduplication.tweet.repository.TextHashRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Status;

import java.util.function.Predicate;

public class ExactTextFilter implements DeduplicateFunction {

    private Logger logger = LoggerFactory.getLogger(ExactTextFilter.class);

    private final TextHashRepository textHashRepository;

    public ExactTextFilter(final TextHashRepository textHashRepository) {
        this.textHashRepository = textHashRepository;
    }

    @Override
    public Observable<Status> apply(Observable<Status> obs) {
        Predicate<Status> filterOutTweetWithSameText = t -> !textHashRepository.textExists(t.getText());
        return obs.filter(filterOutTweetWithSameText::test).doOnNext(t -> textHashRepository.add(t.getText()));
    }
}
