package com.codecool.web.model;

public class Event {
    private int id;
    private int columnid;
    private int taskid;
    private int starttime;
    private int endtime;
    private String description;

    public Event(int id, int columnid, int taskid, int starttime, int endtime, String description) {
        this.id = id;
        this.columnid = columnid;
        this.taskid = taskid;
        this.starttime = starttime;
        this.endtime = endtime;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getColumnid() {
        return columnid;
    }

    public int getTaskid() {
        return taskid;
    }

    public int getStarttime() {
        return starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public String getDescription() {
        return description;
    }
}
