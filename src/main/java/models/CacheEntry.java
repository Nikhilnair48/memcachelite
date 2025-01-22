package models;

public class CacheEntry<T> {
    private final T value;
    private final long expiryTime;

    public CacheEntry(T value, Long ttl) {
        this.value = value;
        if (ttl == null || ttl <= 0) {
            this.expiryTime = Long.MAX_VALUE;
        } else {
            this.expiryTime = System.currentTimeMillis() + ttl;
        }
    }

    public T getValue() {
        return value;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
