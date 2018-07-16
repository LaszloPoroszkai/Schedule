package com.codecool.web.services;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.services.exceptions.IvalidUserIdException;
import com.codecool.web.services.exceptions.SQLErrorDuringScheduleRequestById;
import com.codecool.web.services.exceptions.ScheduleExistsException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ScheduleService implements IScheduleService {
    private final ScheduleDao scheduleDao;
    private Connection connection;


    public ScheduleService(Connection connection) {
        this.scheduleDao = new ScheduleDao(connection);
        this.connection = connection;
    }

    @Override
    public List<Schedule> findSchedulesByUserId(int id) throws SQLErrorDuringScheduleRequestById {
        try {
            return scheduleDao.findSchedulesById(id);
        } catch (SQLException e) {
            throw new SQLErrorDuringScheduleRequestById(e);
        }
    }

    public void addNewSchedule(Integer userId, String name) throws ScheduleExistsException {

        try {
            List<Schedule> schedules = scheduleDao.findSchedulesById(userId);
            for (Schedule sche : schedules) {
                if (sche.getName().equals(name)) {
                    throw new ScheduleExistsException();
                }
            }
            scheduleDao.addNewSchedule(userId, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Schedule updateSchedule(String oldTitle, String newTitle, Integer userId) throws SQLException, SQLErrorDuringScheduleRequestById, ScheduleExistsException{
        List<Schedule> scheds = findSchedulesByUserId(userId);
        for (Schedule sched : scheds) {
            if (sched.getName().equals(newTitle)) {
                throw new ScheduleExistsException();
            }
        }
        Schedule schedule = scheduleDao.updateScheduleTitle(oldTitle, newTitle, userId);
        if (schedule != null) {
            return schedule;
        }
        else {
            throw new SQLException();
        }
    }

    public int parseStringIdToInt(String id) throws IvalidUserIdException {
        try {
            return Integer.parseInt(id);
        } catch (Exception p) {
            p.printStackTrace();
            throw new IvalidUserIdException();
        }

    }

    public void deleteSchedule(Integer id) throws SQLException {
        scheduleDao.deleteScheduleById(id);
    }

}