package com.shekhargulati.deduplication.tweet.repository;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class InmemoryTextHashRepository implements TextHashRepository {

    private final Set<String> hashes = ConcurrentHashMap.newKeySet();

    @Override
    public boolean textExists(final String text) {
        return hashes.contains(hash(text));
    }

    @Override
    public void add(final String text) {
        hashes.add(hash(text));
    }

    @Override
    public Set<String> getHashes() {
        return Collections.unmodifiableSet(hashes);
    }
}
