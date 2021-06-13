package com.shipping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class ApiKeySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApiKeyRequestFilter apiKeyRequestFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        getHttp().csrf().disable().authorizeRequests().antMatchers("/v1/shipping/item/*").authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        getHttp().addFilterBefore(apiKeyRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
