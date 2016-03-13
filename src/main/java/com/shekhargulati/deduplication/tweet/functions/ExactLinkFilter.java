package com.shekhargulati.deduplication.tweet.functions;

import com.shekhargulati.deduplication.tweet.repository.UrlRepository;
import com.shekhargulati.urlcleaner.Options;
import com.shekhargulati.urlcleaner.UrlCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Status;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.shekhargulati.urlcleaner.UrlExtractor.extractUrls;

/**
 * Filters out tweets with exact same links using UrlRepository.
 */
public class ExactLinkFilter implements DeduplicateFunction {

    private Logger logger = LoggerFactory.getLogger(ExactLinkFilter.class);

    private final UrlRepository urlRepository;

    public ExactLinkFilter(final UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Observable<Status> apply(Observable<Status> obs) {
        Predicate<Status> filterOutTweetWithSameUrl = tweet -> {
            List<String> urls = getNormalizedUrls(extractUrls(tweet.getText()));
            return urls.isEmpty() || !urls.stream().anyMatch(urlRepository::urlExists);
        };
        return obs.filter(filterOutTweetWithSameUrl::test).doOnNext(t -> urlRepository.add(getNormalizedUrls(extractUrls(t.getText()))));
    }

    private List<String> getNormalizedUrls(List<String> urls) {
        try {
            return UrlCleaner.normalizeUrl(urls, Options.DEFAULT_OPTIONS);
        } catch (Exception e) {
            logger.error("Error while normalizing urls {}", urls, e);
            return Collections.emptyList();
        }
    }
}
