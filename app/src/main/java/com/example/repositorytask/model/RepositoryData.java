package com.example.repositorytask.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

/**
 * This model is used to get response status from server for some responses
 */
@Entity(tableName = "repository")
public class RepositoryData {

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    public void setCurrentPeriodStars(int currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }

//    public List<BuiltByInfo> getBuiltBy() {
//        return builtBy;
//    }
//
//    public void setBuiltBy(List<BuiltByInfo> builtBy) {
//        this.builtBy = builtBy;
//    }


    @Expose
    private String author;
    @Expose
    @NonNull
    @PrimaryKey
    private String name;
    @Expose
    private String avatar;
    @Expose
    private String url;
    @Expose
    private String description;
    @Expose
    private String language;
    @Expose
    private String languageColor;
    @Expose
    private int stars;
    @Expose
    private int forks;
    @Expose
    private int currentPeriodStars;
//    @Expose
//    private List<BuiltByInfo> builtBy;


    public RepositoryData() {
    }

    @Ignore
    public RepositoryData(String author, String name, String avatar,String url,String description,String language,String languageColor,int stars,int forks,int currentPeriodStars) {
        this.author = author;
        this.name = name;
        this.avatar = avatar;
        this.url = url;
        this.description = description;
        this.language = language;
        this.languageColor = languageColor;
        this.stars = stars;
        this.forks = forks;
        this.currentPeriodStars = currentPeriodStars;

    }


}
