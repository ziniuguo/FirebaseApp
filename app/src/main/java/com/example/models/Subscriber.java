package com.example.models;

import java.util.ArrayList;

public class Subscriber implements Observer {
    //Unique userId generated from singleton User class
    private String userId;

    //List of all subjects subscribed to
    private ArrayList<Subject> subject;

    //Initial topics to follow
    //e.g. English, Math etc
    public Subscriber(String userId) {
        this.userId = userId;
        this.subject = new ArrayList<>();
    }

    @Override
    public void update(String msg) {
        //Notification to user regarding subject that called update(String msg)
    }

    public void subscribe(Subject subject) {
        this.subject.add(subject);
        subject.register(this);
    }

}
