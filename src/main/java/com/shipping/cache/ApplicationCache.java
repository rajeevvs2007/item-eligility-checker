package com.shipping.cache;

public interface ApplicationCache {

    public Object get(String key);
    public void put(String key, Object obj, long ttl);
    public void remove(String key);


}
