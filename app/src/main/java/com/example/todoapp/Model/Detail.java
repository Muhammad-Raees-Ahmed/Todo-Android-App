package com.example.todoapp.Model;

public class Detail {
    public String id;
    public String name;
    public String task;
    public String logopath;
    public String logourl;
    public long createdDate;

    public Detail() {
        //public no-arg constructor needed
    }

    public Detail(String id, String name, String task, String logopath, String logourl, long createdDate) {
        this.id = id;
        this.name = name;
        this.task = task;
        this.logopath = logopath;
        this.logourl = logourl;
        this.createdDate = createdDate;
    }

    // getters set filed in firestore



}


