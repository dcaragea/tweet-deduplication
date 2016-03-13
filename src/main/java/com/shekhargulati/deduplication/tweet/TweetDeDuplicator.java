package com.shekhargulati.deduplication.tweet;

import com.shekhargulati.deduplication.tweet.functions.ExactLinkFilter;
import com.shekhargulati.deduplication.tweet.functions.ExactTextFilter;
import com.shekhargulati.deduplication.tweet.functions.RemoveRetweetTextTransformation;
import com.shekhargulati.deduplication.tweet.functions.TextLowercaseTransformation;
import com.shekhargulati.deduplication.tweet.repository.TextHashRepository;
import com.shekhargulati.deduplication.tweet.repository.UrlRepository;
import com.shekhargulati.reactivex.twitter.TweetStream;
import rx.Observable;
import twitter4j.Status;

import java.util.function.Function;

public class TweetDeDuplicator {

    private final UrlRepository urlRepository;
    private final TextHashRepository textHashRepository;
    private final Function<Observable<Status>, Observable<Status>> pipeline;

    public TweetDeDuplicator(final UrlRepository urlRepository, final TextHashRepository textHashRepository) {
        this.urlRepository = urlRepository;
        this.textHashRepository = textHashRepository;
        this.pipeline = deduplicationPipeline(Function.identity(), Function.identity());
    }

    public TweetDeDuplicator(final UrlRepository urlRepository, final TextHashRepository textHashRepository, final Function<Observable<Status>, Observable<Status>> pipeline) {
        this.urlRepository = urlRepository;
        this.textHashRepository = textHashRepository;
        this.pipeline = pipeline;
    }

    public Observable<Status> deduplicate(final String... searchTerms) {
        return deduplicate(TweetStream.of(searchTerms));
    }

    public Observable<Status> deduplicate(final Observable<Status> tweetObs) {
        return pipeline.apply(tweetObs);
    }

    public Function<Observable<Status>, Observable<Status>> deduplicationPipeline(final Function<Observable<Status>, Observable<Status>> start, final Function<Observable<Status>, Observable<Status>> end) {
        return start
                .andThen(new TextLowercaseTransformation())
                .andThen(new RemoveRetweetTextTransformation())
                .andThen(new ExactLinkFilter(urlRepository))
                .andThen(new ExactTextFilter(textHashRepository))
                .andThen(end);
    }

}
