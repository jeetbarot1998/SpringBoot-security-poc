package com.example.jwt_demo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication Response")
public class AuthResponse {

    @Schema(description = "Response message", example = "Authentication successful")
    private String message;

    @Schema(description = "JWT token (null if authentication fails)",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}