package com.creighbattle.payload;

import lombok.*;

@Data
public class JWTLoginSuccessResponse {

    private boolean success;
    private String token;

    public JWTLoginSuccessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

}
