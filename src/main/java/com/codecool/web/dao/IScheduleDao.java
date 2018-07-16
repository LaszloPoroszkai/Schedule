package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

interface IScheduleDao {
    List<Schedule> findSchedulesById(int userId) throws SQLException;
}
