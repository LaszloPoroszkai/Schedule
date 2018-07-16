package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDao extends AbstractDao implements IScheduleDao {

    public ScheduleDao(Connection connection) {
        super(connection);
    }

    public List<Schedule> findSchedulesById(int userId) throws SQLException {
        List<Schedule> result = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE userid=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(assembleSchedule(resultSet));
                }
            }
        }
        return result;
    }

    public List<Schedule> findScheduleByColumn(int columnid) throws SQLException {
        List<Schedule> result = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE id in (SELECT schid FROM columns WHERE id=?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, columnid);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    result.add(assembleSchedule(resultSet));
                }
            }
        }
        return result;
    }

    public Schedule updateScheduleTitle(String oldTitle, String newTitle, Integer userId) throws SQLException {
        String sql = "UPDATE schedules SET name=? WHERE name=? and userid=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, oldTitle);
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();
        }
        return findScheduleByTitle(newTitle, userId);
    }

    public Schedule findScheduleByTitle(String title, Integer userId) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE name=? AND userid=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return assembleSchedule(resultSet);
                }
            }
        }
        return null;
    }

    public void addNewSchedule(Integer userId, String name) throws SQLException {
        String sql = "INSERT INTO schedules(name, userid) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }


    private Schedule assembleSchedule(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("userid");
        return new Schedule(id, userId, name);

    }
    
    public void deleteScheduleById(Integer scheduleId) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "DELETE FROM events WHERE columnid IN (SELECT id FROM columns WHERE schid = " + Integer.toString(scheduleId) + ");";
            String sql2 = "delete from columns where schid = " + Integer.toString(scheduleId) + ";";
            String sql3 = "delete from schedules where id = " + Integer.toString(scheduleId) + ";";
            statement.addBatch(sql);
            statement.addBatch(sql2);
            statement.addBatch(sql3);
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
