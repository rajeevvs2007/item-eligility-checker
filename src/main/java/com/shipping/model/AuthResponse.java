package com.shipping.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    private final String jwt;

    public AuthResponse(String jwttoken) {
        this.jwt = jwttoken;
    }

    public String getToken() {
        return this.jwt;
    }


}
