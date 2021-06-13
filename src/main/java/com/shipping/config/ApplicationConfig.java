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

    @Value("${shipping.program.rules}")
    public  List<String> programRules;

    @Value("${cache.strategy.item}")
    public String itemCacheStrategy;

    @Value("${cache.strategy.item.ttl}")
    public long itemCacheTTL;

    @Value("${business.admin.portal.username}")
    public String userName;

    @Value("${business.admin.portal.password}")
    public String password;

    @Value("${jwt.authentication.disabled}")
    public boolean jwtAuthenticationDisabled;

    @Value("${valid.apikey}")
    public String validApiKey;

    @Value("${apikey.auth.disabled}")
    public boolean apikeyAuthenticationDisabled;

    public String getValidApiKey() {
        return validApiKey;
    }

    public void setValidApiKey(String validApiKey) {
        this.validApiKey = validApiKey;
    }

    public boolean isApikeyAuthenticationDisabled() {
        return apikeyAuthenticationDisabled;
    }

    public void setApikeyAuthenticationDisabled(boolean apikeyAuthenticationDisabled) {
        this.apikeyAuthenticationDisabled = apikeyAuthenticationDisabled;
    }



    public boolean isJwtAuthenticationDisabled() {
        return jwtAuthenticationDisabled;
    }

    public void setJwtAuthenticationDisabled(boolean jwtAuthenticationDisabled) {
        this.jwtAuthenticationDisabled = jwtAuthenticationDisabled;
    }

    public List<String> getProgramRules() {
        return programRules;
    }

    public void setProgramRules(List<String> programRules) {
        this.programRules = programRules;
    }

    public String getItemCacheStrategy() {
        return itemCacheStrategy;
    }

    public void setItemCacheStrategy(String itemCacheStrategy) {
        this.itemCacheStrategy = itemCacheStrategy;
    }

    public long getItemCacheTTL() {
        return itemCacheTTL;
    }

    public void setItemCacheTTL(long itemCacheTTL) {
        this.itemCacheTTL = itemCacheTTL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:api_error_messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
