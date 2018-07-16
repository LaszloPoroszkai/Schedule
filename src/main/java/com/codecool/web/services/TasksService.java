package com.codecool.web.services;

import com.codecool.web.dao.TasksDao;
import com.codecool.web.model.Task;
import com.codecool.web.services.exceptions.InvalidRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TasksService {
    private Connection connection;

    public TasksService(Connection connection) {
        this.connection = connection;
    }

    public List<Task> getTasksByUser(Integer id) {
        TasksDao td = new TasksDao(connection);
        List<Task> tasks = new ArrayList<>();

        try {
            tasks = td.findTasksByUser(id);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return tasks;
    }

    public Task getTasksById(int id) {
        TasksDao td = new TasksDao(connection);
        try {
            return td.findTasksById(id);
        }catch (SQLException sq){
            sq.printStackTrace();
        }
        return null;
    }


    public void addNewTask(Integer userId, String title) {
        TasksDao td = new TasksDao(connection);
        try {
            td.addNewTask(userId, title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Integer id) throws InvalidRequestException{
        TasksDao td = new TasksDao(connection);
        try {
            if (!td.isInEvents(id)){
            try {
                td.deleteTasksById(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }}
        } catch (SQLException | InvalidRequestException e) {
            e.printStackTrace();
            throw new InvalidRequestException();

        }
    }
}
