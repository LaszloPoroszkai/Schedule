package com.codecool.web.model;

public class Column {
    private int id;
    private int schid;
    private String title;

    public Column(int id, int schid, String title) {
        this.id = id;
        this.schid = schid;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getSchid() {
        return schid;
    }

    public String getTitle() {
        return title;
    }
}
