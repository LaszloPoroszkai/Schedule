package com.codecool.web.dao;

import com.codecool.web.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsDao extends AbstractDao {

    public EventsDao(Connection connection) {
        super(connection);
    }

    public List<Event> findEventsBySchid(Integer schid) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events where columnid in (SELECT id FROM columns where schid=?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, schid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(createEvent(resultSet));
                }
            }
            return events;

        }
    }

    public Event findEventById(Integer id) throws SQLException {
        String sql = "SELECT * FROM events where id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createEvent(resultSet);
                }
            }

        }
        return null;
    }

    public boolean newEvent(Integer taskid, Integer columnid, Integer starttime, Integer endtime, String description) throws SQLException {
        String sql = "INSERT INTO events (taskid, columnid, starttime, endtime, description) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, taskid);
            preparedStatement.setInt(2, columnid);
            preparedStatement.setInt(3, starttime);
            preparedStatement.setInt(4, endtime);
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();
            return true;
        }
    }

    public boolean updateEvent(Integer id, Integer taskid, Integer columnid, Integer starttime, Integer endtime, String description) throws SQLException {
        String sql = "UPDATE events set taskid=?, columnid=?, starttime=?, endtime=?, description=? WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, taskid);
            preparedStatement.setInt(2, columnid);
            preparedStatement.setInt(3, starttime);
            preparedStatement.setInt(4, endtime);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
            return true;
        }
    }

    public boolean deleteEvent(Integer id) throws SQLException {
        String sql = "DELETE FROM events WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        return true;
    }


    public Event createEvent(ResultSet resultSet) throws SQLException, IllegalArgumentException {
        int id = resultSet.getInt("id");
        int columnid = resultSet.getInt("columnid");
        int taskid = resultSet.getInt("taskid");
        int starttime = resultSet.getInt("starttime");
        int endtime = resultSet.getInt("endtime");
        String description = resultSet.getString("description");
        return new Event(id, columnid, taskid, starttime, endtime, description);
    }

}