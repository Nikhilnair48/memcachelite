package service;

import apis.InMemCache;
import models.CacheEntry;
import models.InMemCacheState;

public class CacheManager<T> implements InMemCache<T> {

    private final InMemCacheState<T> state;

    public CacheManager(int maxSize) {
        this.state = new InMemCacheState<>(maxSize);
    }

    @Override
    public synchronized void set(String key, T value, Long ttl) {
        // if key exists, remove from LRU
        if (state.getStorage().containsKey(key)) {
            state.getLruKeys().remove(key);
        }

        // compose and store new entry
        CacheEntry<T> entry = new CacheEntry<>(value, ttl);
        state.getStorage().put(key, entry);

        // Add to the front of the LRU queue
        state.getLruKeys().offerFirst(key);

        // Max size exceeded, evict oldest
        if (state.size() > state.getMaxSize()) {
            String lruKey = state.getLruKeys().pollLast();
            if (lruKey != null) {
                state.getStorage().remove(lruKey);
            }
        }
    }

    @Override
    public synchronized T get(String key) {
        CacheEntry<T> entry = state.getStorage().get(key);
        if (entry == null) {
            return null;
        }

        // If expired, remove from storage and LRU
        if (entry.isExpired()) {
            state.getStorage().remove(key);
            state.getLruKeys().remove(key);
            return null;
        }

        // move key to front of LRU
        state.getLruKeys().remove(key);
        state.getLruKeys().offerFirst(key);

        return entry.getValue();
    }
}
