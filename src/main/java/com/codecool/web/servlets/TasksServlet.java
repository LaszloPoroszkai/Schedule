package com.codecool.web.servlets;

import com.codecool.web.model.Task;
import com.codecool.web.services.TasksService;
import com.codecool.web.services.exceptions.InvalidRequestException;
import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/protected/tasks")
public class TasksServlet extends AbstractServlet {
    private final Logging logger = new Logging(TasksServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TasksService taskser = new TasksService(connection);

            logger.instance().info("TasksServlet: recieved get request with user id: " + req.getParameter("userid"));
            logger.instance().info("TasksServlet: recieved get request with task id: " + req.getParameter("taskid"));

            if (req.getParameter("userid")!=null) {
                List<Task> tasks = taskser.getTasksByUser(Integer.parseInt(req.getParameter("userid")));

                logger.instance().info("Response has been sent back with HTTP_OK by user id");
                sendMessage(resp, HttpServletResponse.SC_FOUND, tasks);

            }else if (req.getParameter("taskid")!=null) {
                List<Task> task = new ArrayList<>();
                task.add(taskser.getTasksById(Integer.parseInt(req.getParameter("taskid"))));

                logger.instance().info("Response has been sent back with HTTP_OK bt task id");
                sendMessage(resp, HttpServletResponse.SC_FOUND, task);
            }
        } catch (SQLException e) {
            logger.instance().error("SQL Exception in the TasksServlet: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer id = Integer.parseInt(req.getParameter("userid"));
            String title = req.getParameter("title");
            TasksService taskser = new TasksService(connection);

            logger.instance().info("TasksServlet: recieved post request with user id: " + id);
            logger.instance().info("TasksServlet: recieved post request with title: " + title);
            logger.instance().info("TasksServlet: recieved post request with taskser: " + taskser);

            taskser.addNewTask(id, title);
            List<Task> tasks = taskser.getTasksByUser(id);


            logger.instance().info("Response sent with Ok from TasksServlet");
            sendMessage(resp, HttpServletResponse.SC_FOUND, tasks);

        } catch (SQLException e) {
            logger.instance().error("SQL Exception in the TasksServlet: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){

            Integer taskId = Integer.parseInt(req.getParameter("taskid"));
            TasksService tasksService = new TasksService(connection);
            tasksService.deleteTask(taskId);

            logger.instance().info("TasksServlet: recieved post request with user task id: " + taskId);

            logger.instance().info("TasksServlet: task deleted by taskid: " + taskId);
            sendTextMessage(resp,HttpServletResponse.SC_OK,"ok");

        } catch (SQLException e) {
            logger.instance().error("SQL Exception during delete in TasksServlet: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp,HttpServletResponse.SC_NO_CONTENT,"No tasks with this id");
        }
        catch (InvalidRequestException ex){
            logger.instance().error("Invalid Request Exception in TasksServlet: " + ex.getMessage());
            ex.printStackTrace();
            sendTextMessage(resp,HttpServletResponse.SC_NO_CONTENT,"This task is in the Schedule, cannot delete");
        }
    }
}