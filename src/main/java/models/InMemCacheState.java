package models;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InMemCacheState<T> {

    private final ConcurrentHashMap<String, CacheEntry<T>> storage;
    private final ConcurrentLinkedDeque<String> lruKeys;
    private final int maxSize;

    public InMemCacheState(int maxSize) {
        this.storage = new ConcurrentHashMap<>();
        this.lruKeys = new ConcurrentLinkedDeque<>();
        this.maxSize = maxSize;
    }

    public InMemCacheState() {
        this(Integer.MAX_VALUE);
    }

    public ConcurrentHashMap<String, CacheEntry<T>> getStorage() {
        return storage;
    }

    public ConcurrentLinkedDeque<String> getLruKeys() {
        return lruKeys;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int size() {
        return storage.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMemCacheState<?> that)) return false;
        return getMaxSize() == that.getMaxSize()
                && Objects.equals(getStorage(), that.getStorage())
                && Objects.equals(getLruKeys(), that.getLruKeys());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStorage(), getLruKeys(), getMaxSize());
    }

    @Override
    public String toString() {
        return "InMemCacheState{" +
                "storage=" + storage +
                ", lruKeys=" + lruKeys +
                ", maxSize=" + maxSize +
                '}';
    }
}
