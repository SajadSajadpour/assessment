package com.example.assessment.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favorites_table")
public class Article {

    private String title;
    private String url;


    @PrimaryKey
    private long id;

    @SerializedName("published_date")
    @ColumnInfo(name = "published_date")
    private String publishedDate;

    @SerializedName("byline")
    private String author;

    public Article() {

    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void generateId() {
        this.id = this.url.hashCode();
    }
}
