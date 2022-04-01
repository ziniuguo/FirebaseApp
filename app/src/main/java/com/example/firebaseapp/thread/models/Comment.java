package com.example.firebaseapp.thread.models;

public class Comment implements Rating {
    private final String userId;
    private final String threadId;
    private String status;
    private int rating;
    private String comment;

    public Comment(String userId, String threadId, String comment) {
        this.userId = userId;
        this.threadId = threadId;
        this.status = "Active";
        this.rating = 0;
        this.comment = comment;
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

    public String getThreadId() {
        return this.threadId;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
