package com.codecool.web.dao;

import com.codecool.web.model.Task;
import com.codecool.web.services.exceptions.InvalidRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TasksDao extends AbstractDao {

    public TasksDao(Connection connection) {
        super(connection);
    }

    public List<Task> findTasksByUser(Integer id) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks where userid=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(createTask(resultSet));
                }
            }
            return tasks;
        }
    }

    public Task findTasksById(int id) throws SQLException {
        String sql = "SELECT * FROM tasks where id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = createTask(resultSet);
                    return task;
                }
            }
            return null;
        }
    }

    public void addNewTask(Integer userId, String title) throws SQLException {
        String sql = "INSERT INTO tasks(title, userid) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println(resultSet);
            }
        }
    }


    public Task createTask(ResultSet resultSet) throws SQLException, IllegalArgumentException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        int userId = resultSet.getInt("userid");
        return new Task(id, userId, title);
    }

    public void deleteTasksById(Integer taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        }
    }

    public boolean isInEvents (Integer taskId) throws SQLException , InvalidRequestException {
        String sql = "select * from events inner join tasks on events.taskid=tasks.id where tasks.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, taskId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                // returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
                if (!resultSet.isBeforeFirst()) {
                    return false;
                }else throw new InvalidRequestException("This task is in the Schedule");
            }
        }
    }
}
