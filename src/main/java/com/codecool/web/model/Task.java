package com.codecool.web.model;

public class Task {
    private int id;
    private int userid;
    private String title;

    public Task(int id, int userid, String title) {
        this.id = id;
        this.userid = userid;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }
}
