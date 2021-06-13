package com.shipping.service.impl;

import com.shipping.cache.ApplicationCache;
import com.shipping.cache.CacheServiceFactory;
import com.shipping.config.ApplicationConfig;
import com.shipping.dao.RuleDAO;
import com.shipping.model.RuleRequest;
import com.shipping.model.RuleResponse;
import com.shipping.service.RegionalShippingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionalShippingRuleServiceImpl implements RegionalShippingRuleService {

    @Autowired
    RuleDAO ruleDAO;

    @Autowired
    ApplicationConfig config;


    @Autowired
    CacheServiceFactory cacheServiceFactory;

    @Override
    public void save(RuleRequest rule) {

         ruleDAO.save(rule);

    }

    @Override
    public boolean update(RuleRequest rule) {

        ApplicationCache cache = cacheServiceFactory.getCacheService(config.itemCacheStrategy);
        boolean ruleUpdated = ruleDAO.update(rule);
        //Perform cache removal for the specific key as the data is stale.
        if (ruleUpdated) cache.remove(rule.getRule());

        return ruleUpdated;

    }

    @Override
    public RuleResponse findById(long id) {

        return ruleDAO.findById(id);
    }

    @Override
    public List<RuleResponse> findAll() {

        return ruleDAO.findAll();
    }

    @Override
    public void remove(long id) {

        ruleDAO.remove(id);
    }
}
