package com.shipping.service.impl;

import com.shipping.cache.ApplicationCache;
import com.shipping.cache.CacheServiceFactory;
import com.shipping.config.ApplicationConfig;
import com.shipping.model.EligibilityRequest;
import com.shipping.service.RegionalShippingService;
import com.shipping.util.ShippingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionalShippingServiceImpl implements RegionalShippingService {

    @Autowired
    ShippingUtil shippingUtil;

    @Override
    public boolean checkItemEligibility(EligibilityRequest eligibilityRequest) {

        return shippingUtil.runRulesForShippingEligibilty(
                eligibilityRequest.getSeller(),eligibilityRequest.getCategory(),eligibilityRequest.getPrice());
    }


}
