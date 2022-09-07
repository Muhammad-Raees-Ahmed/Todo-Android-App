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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}


