package com.shekhargulati.deduplication.tweet;

import com.shekhargulati.deduplication.tweet.repository.TextHashRepository;
import com.shekhargulati.deduplication.tweet.repository.UrlRepository;
import com.shekhargulati.reactivex.twitter.TweetStream;
import rx.Observable;
import twitter4j.Status;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TweetDeduplicatorApp {

    public static void main(String[] args) {
        UrlRepository urlRepository = UrlRepository.getDefaultRepository();
        TextHashRepository textHashRepository = TextHashRepository.getDefaultRepository();
        TweetDeDuplicator tweetDeDuplicator = new TweetDeDuplicator(urlRepository, textHashRepository);

        Observable<Status> tweetsObs = TweetStream.of("DaylightSavingTime");
        tweetsObs.buffer(1, TimeUnit.MINUTES).map(List::size).subscribe(count -> System.out.println(String.format("Tweets per minute without deduplication %d", count)));
        tweetDeDuplicator.deduplicate(tweetsObs).buffer(1, TimeUnit.MINUTES).map(List::size).subscribe(count -> System.out.println(String.format("Tweets per minute with deduplication %d", count)));

    }
}
