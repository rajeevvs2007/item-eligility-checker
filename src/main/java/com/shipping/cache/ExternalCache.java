package com.shipping.cache;

import org.springframework.stereotype.Component;

@Component
public class ExternalCache implements ApplicationCache {


    public Object get(String key){
        return null;
    }

    public void put(String key, Object obj, long ttl) {

    }

    public void remove(String key) {

    }
}
