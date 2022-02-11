package gojek.cache.eviction;

import gojek.cache.exception.KeyNotFoundException;
import gojek.cache.model.Bucket;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class RecentlyUsedEvictionStrategy<K,V> implements IEvictionStrategy<K,V>{
    private Deque<Bucket> queue;
    private int capacity;

    public RecentlyUsedEvictionStrategy(Map<K, Bucket<K,V>> map, int capacity) {
        List<Bucket<K,V>> list = new ArrayList<>();
        map.values().stream().forEach(e -> list.add(e));
        list.sort((e1,e2)-> e1.getUsedOn().compareTo(e2.getUsedOn()) > 0 ? 1: -1);

        this.queue= new LinkedBlockingDeque<>();
        list.forEach(e-> queue.add(e));
        this.capacity= capacity;
    }

    @Override
    public V get(Map<K, Bucket<K, V>> map, K key) throws KeyNotFoundException {
        /**
         * s1 : inc usedon, hit
         * s2 : update queue
         */

        if(!map.containsKey(key)){
            throw new KeyNotFoundException(key.toString());
        }

        //TODO:  Abstract this as UpdateMeta
        Bucket<K,V> bucket= map.get(key);
        bucket.setHit(bucket.getHit()+1);
        bucket.setUsedOn(Date.from(Instant.now()));

        queue.remove(bucket);
        queue.addFirst(bucket);


        return bucket.getValue();
    }

    @Override
    public void put(Map<K, Bucket<K, V>> map, K key, V value) {
        if(map.containsKey(key)){
            return;
        }

        /**
         * s1: check capacity
         * s2: evict if valid
         * s3: then add
         */

        if(map.size() >= capacity){//evict
            evict(map);
        }

        //adding in bucket
        Bucket<K,V> bucket= new Bucket<>(key,value);
        queue.addFirst(bucket);
        map.put(key,bucket);
    }

    private void evict(Map<K, Bucket<K, V>> map){
        Bucket<K,V> bucket= queue.removeLast();
        System.out.println("Evicting key: "+ bucket.getKey());
        map.remove(bucket.getKey());
    }
}
