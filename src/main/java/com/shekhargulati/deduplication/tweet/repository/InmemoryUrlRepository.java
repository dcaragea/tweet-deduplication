package com.shekhargulati.deduplication.tweet.repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InmemoryUrlRepository implements UrlRepository {

    private final Set<String> urlCache = ConcurrentHashMap.newKeySet();

    @Override
    public boolean urlExists(final String url) {
        return urlCache.contains(url);
    }

    @Override
    public void add(String url) {
        urlCache.add(url);
    }

    @Override
    public void add(List<String> urls) {
        urlCache.addAll(urls);
    }

    @Override
    public Set<String> getUrlCache() {
        return Collections.unmodifiableSet(urlCache);
    }
}
