package com.rejnowski.bluemedia.db;

import javax.persistence.*;

@Entity
@Table(name = "websites")
public class WebsiteResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "page_url")
    private String pageUrl;

    @Column(name = "page_source")
    private String pageSource;

    @Column
    private String description;

    private long timestamp;

    public WebsiteResource() {
    }

    public WebsiteResource(String pageUrl, String pageSource, String description, long timestamp) {
        this.pageUrl = pageUrl;
        this.pageSource = pageSource;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageSource() {
        return pageSource;
    }

    public void setPageSource(String pageSource) {
        this.pageSource = pageSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
