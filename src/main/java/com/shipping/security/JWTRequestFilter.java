package com.shipping.security;

import com.shipping.config.ApplicationConfig;
import com.shipping.service.impl.UserManagementService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtTokenUtil;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private ApplicationConfig config;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestAuthHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        boolean invalidToken = false;
        if (requestAuthHeader != null && requestAuthHeader.startsWith("Bearer ")) {
            jwt = requestAuthHeader.substring(7);
            try {

                username = jwtTokenUtil.getUsernameFromToken(jwt);

            } catch (IllegalArgumentException e) {
                invalidToken=true;
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                invalidToken=true;
                System.out.println("JWT Token has expired");
            } catch (Exception e) {
                invalidToken=true;
                System.out.println("Unable to get JWT Token");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userManagementService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails) || config.isJwtAuthenticationDisabled()) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {

                invalidToken=true;

            }
        }

        if (invalidToken){

            HttpServletResponse httpResponse = (HttpServletResponse) httpServletResponse;
            httpResponse.setStatus(401);
            httpResponse.getWriter().write("Invalid JWT Token");
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.startsWith("/v1/rules");
    }

}
