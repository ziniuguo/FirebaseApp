package com.example.models;

import java.sql.Timestamp;

public class ThreadClass implements Rating {
    private final String userId;
    private String status;
    private int rating;
    private String title;
    private String thread;
    private String time;

    public ThreadClass(String userId, String title, String thread, String time) {
        this.userId = userId;
        this.status = "Active";
        this.title = title;
        this.thread = thread;
        this.time = time;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getRating() {
        return this.rating;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setRating(int difference) {
        this.rating += difference;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThread() {
        return this.thread;
    }

    public String getTime() {return this.time;}

    public void setThread(String thread) {
        this.thread = thread;
    }
}
