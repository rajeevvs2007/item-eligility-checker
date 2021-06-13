package com.shipping.util;

import com.shipping.cache.ApplicationCache;
import com.shipping.cache.CacheServiceFactory;
import com.shipping.config.ApplicationConfig;
import com.shipping.dao.RuleDAO;
import com.shipping.model.RuleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.StyleConstants;
import java.util.Arrays;
import java.util.List;

@Component
public class ShippingUtil {

    Logger logger = LoggerFactory.getLogger(ShippingUtil.class);

    @Autowired
    ApplicationConfig config;


    @Autowired
    CacheServiceFactory cacheServiceFactory;

    @Autowired
    RuleDAO ruleDAO;


    /**
     * This util method check if the seller , category and price is in the approved list/limits
     * @param seller
     * @param category
     * @param price
     * @return true if item is eligible for new shipping program
     */
    public boolean runRulesForShippingEligibilty(String seller, int category, double price) {

        logger.debug("seller {}, category {}, price {}", seller,category,price);

        List<String> approvedSellers = null;
        List<String> approvedCategories = null;
        Double approvedPrice = null;


        ApplicationCache cache = cacheServiceFactory.getCacheService(config.itemCacheStrategy);

        //check the rule is found in cache , if not fetch from database and update the cache.
        if(cache.get(ShippingConstants.CACHE_KEY_APPROVED_SELLERS) == null) {
            RuleResponse ruleFromDB = ruleDAO.findRuleByName(ShippingConstants.CACHE_KEY_APPROVED_SELLERS);
            if(ruleFromDB != null) {
                approvedSellers = Arrays.asList(ruleFromDB.getValue().split(","));
                cache.put(ShippingConstants.CACHE_KEY_APPROVED_SELLERS, approvedSellers ,config.getItemCacheTTL());
            }

        }else {
            approvedSellers = (List<String>) cache.get(ShippingConstants.CACHE_KEY_APPROVED_SELLERS);
        }

        if(cache.get(ShippingConstants.CACHE_KEY_APPROVED_CATEGORIES) == null) {
            RuleResponse ruleFromDB = ruleDAO.findRuleByName(ShippingConstants.CACHE_KEY_APPROVED_CATEGORIES);
            if(ruleFromDB != null) {
                approvedCategories = Arrays.asList(ruleFromDB.getValue().split(","));
                cache.put(ShippingConstants.CACHE_KEY_APPROVED_CATEGORIES, approvedCategories ,config.getItemCacheTTL());
            }

        } else {
            approvedCategories = (List<String>) cache.get(ShippingConstants.CACHE_KEY_APPROVED_CATEGORIES);
        }


        if(cache.get(ShippingConstants.CACHE_KEY_APPROVED_PRICE) == null) {
            RuleResponse ruleFromDB = ruleDAO.findRuleByName(ShippingConstants.CACHE_KEY_APPROVED_PRICE);
            if(ruleFromDB != null) {
                approvedPrice = Double.valueOf(ruleFromDB.getValue());
                cache.put(ShippingConstants.CACHE_KEY_APPROVED_PRICE, approvedPrice ,config.getItemCacheTTL());
            }

        }else {
            approvedPrice = (Double) cache.get(ShippingConstants.CACHE_KEY_APPROVED_PRICE);
        }


        if(approvedSellers != null && approvedSellers.contains(seller) &&
                approvedCategories != null && approvedCategories.contains(String.valueOf(category))
                && approvedPrice != null && price >= approvedPrice) {
            logger.info("ITEM ELIGIBLE");
            return true;
        }

        return false;
    }

    public String generateCacheKey(String title, String seller, int category, double price) {
        return new StringBuilder().append(title).append(seller).append(category).append(price).toString();
    }
}
