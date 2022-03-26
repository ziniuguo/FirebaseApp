package com.example.models;

public class ThreadClass implements Rating {
    private final String userId;
    private String status;
    private int rating;
    private String title;
    private String thread;

    public ThreadClass(String userId, String title, String thread) {
        this.userId = userId;
        this.status = "Active";
        this.title = title;
        this.thread = thread;
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

    public void setThread(String thread) {
        this.thread = thread;
    }
}
