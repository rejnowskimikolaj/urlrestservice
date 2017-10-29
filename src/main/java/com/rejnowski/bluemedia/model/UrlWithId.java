package com.rejnowski.bluemedia.model;

public class UrlWithId {
    private String url;
    private int id;

    public UrlWithId(String url, int id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
