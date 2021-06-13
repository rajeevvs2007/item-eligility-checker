package com.shipping.security;

import com.shipping.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationConfig config;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = getApiKey(httpServletRequest);

        if(apiKey != null) {
            if(apiKey.equals(config.getValidApiKey())) { // Perform DB lookup or cache lookup

                ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(apiToken);
            } else {

                HttpServletResponse httpResponse = (HttpServletResponse) httpServletResponse;
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid API Key");
                return;
            }
        } else if (config.isApikeyAuthenticationDisabled()) {

            ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken("testKey", AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(apiToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String getApiKey(HttpServletRequest httpRequest) {

        return httpRequest.getHeader("X-API-KEY");
    }
}
