package com.rejnowski.bluemedia.rest_communication.model;

public class UrlPostRequestResponse {
    String message;

    public UrlPostRequestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
