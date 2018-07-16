package com.codecool.web.services;

import com.codecool.web.dao.EventsDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Event;
import com.codecool.web.model.Schedule;
import com.codecool.web.services.exceptions.EventsOverlapException;
import com.codecool.web.services.exceptions.TaskAddedAlreadyException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private Connection connection;

    public EventService(Connection connection) {
        this.connection = connection;
    }

    public List<Event> getEventsBySchid(Integer schid) {
        EventsDao ed = new EventsDao(connection);
        List<Event> events = new ArrayList<>();

        try {
            events = ed.findEventsBySchid(schid);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return events;
    }

    public Event getEventById(Integer id) {
        EventsDao ed = new EventsDao(connection);
        try {
            return ed.findEventById(id);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return null;
    }

    public List<Schedule> addEvent(Integer taskid, Integer columnid, Integer starttime, Integer endtime, String description) throws TaskAddedAlreadyException, EventsOverlapException {
        EventsDao ed = new EventsDao(connection);
        ScheduleDao sd = new ScheduleDao(connection);
        List<Schedule> sched = new ArrayList<>();

        try {
            sched = sd.findScheduleByColumn(columnid);
            List<Event> currEvs = ed.findEventsBySchid(sched.get(0).getId());
            for (Event event : currEvs) {
                if (event.getTaskid() == taskid) {
                    throw new TaskAddedAlreadyException();
                }

                if(event.getColumnid() == columnid && ((event.getStarttime()<=starttime&&starttime<event.getEndtime())||
                        (event.getStarttime()<endtime&&endtime<=event.getEndtime()))){
                    throw new EventsOverlapException();
                }
            }

            ed.newEvent(taskid, columnid, starttime, endtime, description);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return sched;
    }

    public List<Schedule> updateEvent(Integer id, Integer taskid, Integer columnid, Integer starttime, Integer endtime, String description) throws TaskAddedAlreadyException, EventsOverlapException {
        EventsDao ed = new EventsDao(connection);
        ScheduleDao sd = new ScheduleDao(connection);
        List<Schedule> sched = new ArrayList<>();

        try {
            sched = sd.findScheduleByColumn(columnid);
            List<Event> currEvs = ed.findEventsBySchid(sched.get(0).getId());
            for (Event event : currEvs) {
                if ((event.getTaskid() == taskid) && (event.getId() != id)) {
                    throw new TaskAddedAlreadyException();
                }
                if(event.getColumnid() == columnid && event.getId() != id && ((event.getStarttime()<=starttime&&starttime<event.getEndtime())||
                        (event.getStarttime()<endtime&&endtime<=event.getEndtime()))){
                    throw new EventsOverlapException();
                }
            }

            ed.updateEvent(id, taskid, columnid, starttime, endtime, description);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return sched;
    }

    public List<Schedule> deleteEvent(Integer id, Integer columnid) {
        EventsDao ed = new EventsDao(connection);
        ScheduleDao sd = new ScheduleDao(connection);
        List<Schedule> sched = new ArrayList<>();

        try {
            sched = sd.findScheduleByColumn(columnid);
            ed.deleteEvent(id);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return sched;

    }
}
