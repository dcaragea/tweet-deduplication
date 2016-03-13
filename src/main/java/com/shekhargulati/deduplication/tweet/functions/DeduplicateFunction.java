package com.shekhargulati.deduplication.tweet.functions;

import rx.Observable;
import twitter4j.Status;

import java.util.function.Function;

/**
 * Perform one deduplication action and hand over to the next
 */
public interface DeduplicateFunction extends Function<Observable<Status>, Observable<Status>> {

}
