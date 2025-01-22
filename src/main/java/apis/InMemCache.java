package apis;

public interface InMemCache<T> {
    void set(String key, T value, Long ttl);
    T get(String key);
}
