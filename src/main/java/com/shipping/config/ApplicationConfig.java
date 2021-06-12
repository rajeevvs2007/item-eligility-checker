package com.shipping.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${shipping.program.approved.sellers}")
    public  List<String> approvedSellers;

    @Value("${shipping.program.approved.categories}")
    public List<Integer> approvedCategories;

    @Value("${shipping.program.approved.price}")
    public double approvedPriceLimit;

    @Value("${cache.strategy.item}")
    public String itemCacheStrategy;

    @Value("${cache.strategy.item.ttl}")
    public long itemCacheTTL;


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:api_error_messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
