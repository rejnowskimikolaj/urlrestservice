package com.rejnowski.bluemedia.model;

import com.rejnowski.bluemedia.db.WebsiteResource;

import java.util.List;

public class WebsiteGetResponse {
    String message;

    List<WebsiteResource> websites;

    public WebsiteGetResponse(String message, List<WebsiteResource> websites) {
        this.message = message;
        this.websites = websites;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WebsiteResource> getWebsites() {
        return websites;
    }

    public void setWebsites(List<WebsiteResource> websites) {
        this.websites = websites;
    }
}
