package com.example.models;

public interface Subject {
    void register(Observer o);
    void unregister(Observer o);
    void notifyObservers();
}
