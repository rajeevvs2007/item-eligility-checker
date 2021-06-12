package com.shipping.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheServiceFactory {

    @Autowired
    ExternalCache externalCache;

    @Autowired
    InMemoryCache inMemoryCache;


    public ApplicationCache getCacheService(String cacheStrategy){

        ApplicationCache cache;

        switch (cacheStrategy) {

            case "REDIS" : cache = externalCache;
                            break;
            case "LOCAL" : cache = inMemoryCache;
                            break;
            default :      cache=inMemoryCache;
                            break;


        }
        return cache;
    }


}
