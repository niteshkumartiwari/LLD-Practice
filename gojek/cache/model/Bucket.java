package gojek.cache.model;

import java.time.Instant;
import java.util.Date;

public class Bucket<K,V> {
    private K key;
    private V value;
    private Date updatedOn;
    private Date usedOn;
    private int hit;

    public Bucket(K key, V value) {
        this.key = key;
        this.value = value;
        this.updatedOn= Date.from(Instant.now());
        this.hit=0;
        this.usedOn= Date.from(Instant.now());
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public Date getUsedOn() {
        return usedOn;
    }

    public void setUsedOn(Date usedOn) {
        this.usedOn = usedOn;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }
}
