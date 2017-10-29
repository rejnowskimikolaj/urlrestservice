package com.rejnowski.bluemedia.db.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sessions")
public class TokenSession {

    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "token")
    private String token;
    @Column(name = "timestamp")
    private long timestamp;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
