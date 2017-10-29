package com.rejnowski.bluemedia.rest_communication.model;

public class LoginResponse {

    String token;
    String message;

    public String getMessage() {
        return message;
    }

    public LoginResponse(String message,String token ) {
        this.token = token;
        this.message = message;
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
