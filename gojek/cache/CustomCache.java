package gojek.cache;

import gojek.cache.eviction.IEvictionStrategy;
import gojek.cache.eviction.RecentlyUsedEvictionStrategy;
import gojek.cache.exception.KeyNotFoundException;
import gojek.cache.model.Bucket;
import gojek.cache.model.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CustomCache<K,V> implements ICache<K,V> {

    private IEvictionStrategy<K,V> evictionStrategy;
    private int capacity;
    private Map<K, Bucket<K,V>> map;

    public CustomCache(int capacity) {
        this.capacity = capacity;
        this.map= new ConcurrentHashMap<>();
        this.evictionStrategy= new RecentlyUsedEvictionStrategy<K,V>(map, capacity);
    }

    @Override
    public V get(K key) throws KeyNotFoundException {
        return evictionStrategy.get(map, key);
    }

    @Override
    public void put(K key, V value) {
        evictionStrategy.put(map, key, value);
    }

    public void setEvictionStrategy(IEvictionStrategy<K,V> evictionStrategy) {
        this.evictionStrategy = evictionStrategy;
    }

    public Map<K, Bucket<K, V>> getMap() {
        return map;
    }

    public void setMap(Map<K, Bucket<K, V>> map) {
        this.map = map;
    }
}
