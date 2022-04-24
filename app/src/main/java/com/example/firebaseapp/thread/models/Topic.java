package com.example.firebaseapp.thread.models;

import java.util.ArrayList;

public class Topic implements Subject {
    private final int threadId;
    private final ArrayList<Observer> observers;

    //Update msg, i.e. what user sees when something happens
    private String msg;

    public Topic(int threadId) {
        this.threadId = threadId;
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            o.update(msg);
        }
    }

    //Some other class calls postMessage(String msg)
    //and that class is a listener if the value of
    // the thread or comment changes in the database,
    // i.e. the post was edited,
    //or the comments list of the thread increased
    // i.e. someone commented on the post
    // then the msg will reflect userId edited a post/commented on a post
    public void postMessage(String msg) {
        this.msg = msg;
        notifyObservers();
    }
}
