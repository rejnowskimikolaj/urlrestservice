package com.rejnowski.bluemedia.rest_communication.model;

import java.util.List;

public class UrlsGetResponse {
    private String message;
    private List<UrlWithId> urls;

    public UrlsGetResponse(String message, List<UrlWithId> urls) {
        this.message = message;
        this.urls = urls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UrlWithId> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlWithId> urls) {
        this.urls = urls;
    }
}
