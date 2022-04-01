package com.example.firebaseapp.thread.models;

public interface Subject {
    void register(Observer o);
    void unregister(Observer o);
    void notifyObservers();
}
