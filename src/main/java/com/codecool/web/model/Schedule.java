package com.codecool.web.model;

public class Schedule {
    private int id;
    private int userid;
    private String name;

    public Schedule(int id, int userid, String name) {
        this.id = id;
        this.userid = userid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }
}
