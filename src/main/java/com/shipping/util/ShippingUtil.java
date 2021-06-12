package com.shipping.util;

import com.shipping.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShippingUtil {

    Logger logger = LoggerFactory.getLogger(ShippingUtil.class);

    @Autowired
    ApplicationConfig config;

    /**
     * This util method check if the seller , category and price is in the approved list/limits
     * @param seller
     * @param category
     * @param price
     * @return true if item is eligible for new shipping program
     */
    public boolean runRulesForShippingEligibilty(String seller, int category, double price) {

        logger.debug("seller {}, category {}, price {}", seller,category,price);

        if(config.approvedSellers.contains(seller) && config.approvedCategories.contains(category)
                && price >= config.approvedPriceLimit) {
            logger.info("ITEM ELIGIBLE");
            return true;
        }

        return false;
    }

    public String generateCacheKey(String title, String seller, int category, double price) {
        return new StringBuilder().append(title).append(seller).append(category).append(price).toString();
    }
}
