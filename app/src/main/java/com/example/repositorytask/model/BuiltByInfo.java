package com.example.repositorytask.model;

import com.google.gson.annotations.Expose;


public class BuiltByInfo {

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Expose
    private String href;
    @Expose
    private String avatar;
    @Expose
    private String username;
}
