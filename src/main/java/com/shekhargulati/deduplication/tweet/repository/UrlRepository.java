package com.shekhargulati.deduplication.tweet.repository;

import java.util.List;
import java.util.Set;

public interface UrlRepository {

    static UrlRepository getDefaultRepository() {
        return new InmemoryUrlRepository();
    }

    boolean urlExists(String url);

    void add(String url);

    void add(List<String> urls);

    Set<String> getUrlCache();
}
