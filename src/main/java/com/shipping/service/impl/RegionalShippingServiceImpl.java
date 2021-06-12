package com.shipping.service.impl;

import com.shipping.cache.ApplicationCache;
import com.shipping.cache.CacheServiceFactory;
import com.shipping.config.ApplicationConfig;
import com.shipping.model.EligibilityRequest;
import com.shipping.service.RegionalShippingService;
import com.shipping.util.ShippingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegionalShippingServiceImpl implements RegionalShippingService {

    @Autowired
    ShippingUtil shippingUtil;

    @Autowired
    CacheServiceFactory cacheServiceFactory;

    @Autowired
    ApplicationConfig config;


    @Override
    public boolean checkItemEligibility(EligibilityRequest eligibilityRequest) {

        boolean eligible;

        ApplicationCache cache = cacheServiceFactory.getCacheService(config.itemCacheStrategy);

        String cacheKey = shippingUtil.generateCacheKey(eligibilityRequest.getTitle(),eligibilityRequest.getSeller()
                ,eligibilityRequest.getCategory(),eligibilityRequest.getPrice());

        if(cache.get(cacheKey) != null) {
            eligible =  (Boolean)cache.get(cacheKey);
        } else {
            eligible = shippingUtil.runRulesForShippingEligibilty(
                    eligibilityRequest.getSeller(),eligibilityRequest.getCategory(),eligibilityRequest.getPrice());
            cache.put(cacheKey,Boolean.valueOf(eligible), config.itemCacheTTL);
        }


        return eligible;
    }


}
