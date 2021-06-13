package com.shipping.service.impl;

import com.shipping.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserManagementService implements UserDetailsService {

    @Autowired
    ApplicationConfig config;


    @Override
    public UserDetails loadUserByUsername(String requestUserName) throws UsernameNotFoundException {

        String userName = config.getUserName(); // ToDO we need get this info from database and cache it

        if (userName.equals(requestUserName)) {
            return new User(userName, config.password,new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }
    }
}
