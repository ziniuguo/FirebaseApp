package com.example.firebaseapp.thread.models;

public interface Rating {
    String getStatus();
    int getRating();
    void setStatus(String status);
    void setRating(int difference);
}
