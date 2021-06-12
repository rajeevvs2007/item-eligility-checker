package com.shipping.cache;

import com.shipping.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class InMemoryCache implements ApplicationCache {

    Logger logger = LoggerFactory.getLogger(InMemoryCache.class);


    private Map<String,Object> cache = new ConcurrentHashMap<>();

    @Autowired
    ApplicationConfig applicationConfig;

    /**
     *
     * @param key - cache key
     * @param val - value as object
     * @param ttl - time to live in milliseconds
     *
     */
    public void put(String key, Object val, long ttl) {

        if (Objects.isNull(key) || Objects.isNull(val)) {
            logger.warn("cache key or value null");
            return;
        }
        cache.put(key,createCacheItem(key, val, ttl));

    }


    public Object get(String key){

        if (key == null){
            logger.warn("cache key null");
        }

        Object obj = cache.get(key);

        if(obj instanceof CacheItem) {
            CacheItem cachItem = (CacheItem) obj;
            LocalDateTime currentTS = LocalDateTime.now();
            LocalDateTime cacheTS = cachItem.getTimeToLive();

            if(cacheTS.isAfter(currentTS)) {
                return cachItem;
            } else {
                cache.remove(key);
            }
        }

        return null;
    }

    /**
     *
     * @param key
     */
    public void remove(String key){

    }

    private Object createCacheItem(String key, Object obj, long ttl) {

        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.plusSeconds(TimeUnit.MILLISECONDS.toSeconds(ttl));
        return new CacheItem(obj,ldt,key);
    }

    private class CacheItem{
        private Object val;
        private LocalDateTime timeToLive;
        private String key;

        CacheItem(Object val, LocalDateTime timeToLive, String key) {
            this.val = val;
            this.timeToLive = timeToLive;
            this.key = key;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public LocalDateTime getTimeToLive() {
            return timeToLive;
        }

        public void setTimeToLive(LocalDateTime timeToLive) {
            this.timeToLive = timeToLive;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
