# MemCacheLite

A generic, thread-safe in-memory cache with the following features:
- Least Recently Used (LRU): least accessed element is removed upon reaching the limit.
- Time-to-Live (TTL): allowing entries to expire after a specified duration. Also handles the absence of TTL.
- Thread-safe operations: an initial version with synchronized get & set along with Concurrent Map (storage) and Queue (LRU)
- Maximum entries: user may set the upper cap
- Support any data structure using Generics

## Project structure

```
memcachelite/
│-- src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── apis/
│   │   │   │   └── InMemCache.java      # Cache interface 
│   │   │   ├── models/
│   │   │   │   ├── CacheEntry.java      # Cache value and TTL encapsulation
│   │   │   │   ├── InMemCacheState.java # Stores cache entries and LRU list
│   │   │   ├── service/
│   │   │   │   └── CacheManager.java    # Manges the internal state via get/set methods
│   ├── test/
│   │   ├── java/
│   │   │   ├── service/
│   │   │   │   └── CacheManagerTest.java
│-- pom.xml
│-- README.md
```

### Clone and run tests

```bash
   git clone git@github.com:Nikhilnair48/memcachelite.git
   cd memcachelite
   mvn clean install
   mvn test
```