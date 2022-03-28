package com.example.firebaseapp.thread.models;

public class User implements Rating {
    protected final String displayName;
    protected String status;
    protected int rating;

    User(String displayName) {
        this.displayName = displayName;
        this.rating = 0;
        this.status = "Active";
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

    public String getDisplayName() {
        return this.displayName;
    }

    public void upVote(Rating ratingObject) {
        ratingObject.setRating(1);
    }

    public void downVote(Rating ratingObject) {
        ratingObject.setRating(-1);
    }

    public void report(ThreadClass threadClass) {
//        firebase update reports thread
    }

    public void report(Comment comment) {
//        firebase update reports comment
    }

    public void report(User user) {
//        firebase update reports user
    }
}
