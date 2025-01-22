package service;

import apis.InMemCache;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheManagerTest {

    private InMemCache<String> cache;

    @BeforeEach
    void setUp() {
        cache = new CacheManager<>(3);
    }

    @Test
    @DisplayName("Basic Set and Get Test")
    void testSetAndGet() {
        cache.set("key1", "value1", 5000L);
        cache.set("key2", "value2", 5000L);

        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    @DisplayName("Expired Entry Test")
    void testExpiration() throws InterruptedException {
        cache.set("key1", "value1", 1000L);

        assertEquals("value1", cache.get("key1"));

        Thread.sleep(1500);

        assertNull(cache.get("key1"));
    }

    @Test
    @DisplayName("LRU Eviction Test")
    void testLRUEviction() {
        cache.set("key1", "value1", 5000L);
        cache.set("key2", "value2", 5000L);
        cache.set("key3", "value3", 5000L);

        // Mark key1 and key2 as recently used
        cache.get("key1");
        cache.get("key2");

        // This entry will exceed cache limit
        cache.set("key4", "value4", 5000L);

        // key3 must be evicted
        assertNull(cache.get("key3"));
        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
        assertEquals("value4", cache.get("key4"));
    }

    @Test
    @DisplayName("Overwrite Key Test")
    void testOverwriteKey() {
        cache.set("key1", "value1", 5000L);
        cache.set("key1", "newValue1", 5000L);

        assertEquals("newValue1", cache.get("key1"));
    }

    @Test
    @DisplayName("No Expiration Entry Test")
    void testNoExpirationEntry() {
        cache.set("key1", "value1", null);

        assertEquals("value1", cache.get("key1"));
    }

    @Test
    @DisplayName("Cache Size Enforcement Test")
    void testCacheSizeLimit() {
        cache.set("key1", "value1", 5000L);
        cache.set("key2", "value2", 5000L);
        cache.set("key3", "value3", 5000L);
        cache.set("key4", "value4", 5000L);

        assertNull(cache.get("key1"));
        assertNotNull(cache.get("key2"));
    }

    @Test
    @DisplayName("Thread Safety Test")
    void testConcurrentAccess() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                cache.set("key" + index, "value" + index, 5000L);
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();
        int nonNullValues = 0;
        for (int i = 0; i < threadCount; i++) {
            if(cache.get("key" + i) != null)
                nonNullValues++;
        }
        assertEquals(3, nonNullValues);
    }

    @Test
    @DisplayName("Multiple Set Calls with TTLs")
    void testMultipleSetWithTTL() throws InterruptedException {
        cache.set("key1", "value1", 2000L);
        cache.set("key2", "value2", 3000L);
        cache.set("key3", "value3", 4000L);

        Thread.sleep(2500);

        assertNull(cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
        assertEquals("value3", cache.get("key3"));
    }

    @Test
    @DisplayName("Fetching Non-Existent Key")
    void testGetNonExistentKey() {
        assertNull(cache.get("nonExistent"));
    }

    @Test
    @DisplayName("Verify TTL Update on Set")
    void testTTLUpdate() throws InterruptedException {
        cache.set("key1", "value1", 1000L);
        Thread.sleep(500);
        cache.set("key1", "value1", 3000L);
        Thread.sleep(1200);
        assertEquals("value1", cache.get("key1"));
    }
}

