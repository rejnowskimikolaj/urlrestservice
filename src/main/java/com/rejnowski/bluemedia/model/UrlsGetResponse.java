package com.rejnowski.bluemedia.model;

import java.util.List;

public class UrlsGetResponse {
    private String message;
    private List<String> urls;

    public UrlsGetResponse(String message, List<String> urls) {
        this.message = message;
        this.urls = urls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
