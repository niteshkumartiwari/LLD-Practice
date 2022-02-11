package gojek.cache.eviction;

import gojek.cache.model.Bucket;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Map;

public interface IEvictionStrategy<K,V> {
    public V get(Map<K, Bucket<K,V>> map, K key) throws KeyAlreadyExistsException;
    public void put(Map<K, Bucket<K,V>> map, K Key, V value);
}
