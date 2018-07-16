package com.codecool.web.servlets;


import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.services.IScheduleService;
import com.codecool.web.services.ScheduleService;
import com.codecool.web.services.TasksService;
import com.codecool.web.services.exceptions.IvalidUserIdException;
import com.codecool.web.services.exceptions.SQLErrorDuringScheduleRequestById;
import com.codecool.web.services.exceptions.ScheduleExistsException;
import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/protected/schedule")
public class ScheduleServlet extends AbstractServlet {
    private final Logging logger = new Logging(ScheduleServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = fetchRequestUrl(req);

        logger.instance().info("Url fethced in ScheduleServlet: " + url);

        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new ScheduleService(connection);
            List<Schedule> currentSchedule;
            try {
                int intUsedId = scheduleService.parseStringIdToInt(fetchUserIdFromUrl(url));
                currentSchedule = scheduleService.findSchedulesByUserId(intUsedId);

                if(currentSchedule.size() == 0){
                    logger.instance().warn("No schedule found in ScheduleServlet");
                    logger.instance().info("Response sent back from ScheduleServlet with HTTP code 204");
                    sendTextMessage(resp, HttpServletResponse.SC_NO_CONTENT, "No schedule added yet");
                }
                else {
                    logger.instance().info("Response sent back from ScheduleServlet with HTTP code 200");
                    sendMessage(resp, HttpServletResponse.SC_OK, currentSchedule);
                }
            } catch (IvalidUserIdException e) {
                logger.instance().error("Invalid user in ScheduleServlet");
                sendTextMessage(resp, 422, "Unprocessed entity error. Check if the client is sending the correct query string");
            } catch (SQLErrorDuringScheduleRequestById sq) {
                logger.instance().error("SQL error during query reqest of schedule by id: " + sq.getMessage());
                sq.printStackTrace();
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HTTP ERROR 500 -- Database error");
            }
        } catch (SQLException e) {
            logger.instance().error("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HTTP ERROR 500 -- Database error");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer id = Integer.parseInt(req.getParameter("id"));
            logger.instance().warn("IN SCHEDULE SERVLET: " + id);
            String name = req.getParameter("name");
            ScheduleService schedser = new ScheduleService(connection);

            logger.instance().info("ScheduleServlet: recieved id in post request: " + id);
            logger.instance().info("ScheduleServlet: recieved name in post request: " + name);

            schedser.addNewSchedule(id, name);
            List<Schedule> scheds = schedser.findSchedulesByUserId(id);

            logger.instance().info("ScheduleServlet: all schedules size: " + scheds.size());

            sendMessage(resp, HttpServletResponse.SC_OK, scheds);
            logger.instance().info("ScheduleServlet: response sent from post method with response statud HTTP OK");

        } catch (SQLException e) {
            logger.instance().error("SQL Exception in ScheduleServlet: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        } catch (SQLErrorDuringScheduleRequestById sql) {
            logger.instance().error("SQL error during query reqest of schedule by id in post method: " + sql.getMessage());
            sql.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        } catch (ScheduleExistsException sche) {
            logger.instance().error("Schedule with same name exists already!");
            sche.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Schedule with this name exists already");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            String oldTitle = req.getParameter("title");
            String newTitle = req.getParameter("newTitle");
            Integer userId = Integer.parseInt(req.getParameter("userId"));
            ScheduleService schedser = new ScheduleService(connection);

            logger.instance().info("ScheduleServlet: recieved old title in put request: " + oldTitle);
            logger.instance().info("ScheduleServlet: recieved new title in put request: " + newTitle);

            Schedule sched = schedser.updateSchedule(oldTitle, newTitle, userId);

            logger.instance().info("ScheduleServlet: response sent from post method with response statud HTTP OK");
            sendMessage(resp, HttpServletResponse.SC_OK, sched);
            doGet(req, resp);

        } catch (SQLException e) {
            logger.instance().error("SQL Exception in ScheduleServlet: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        } catch (SQLErrorDuringScheduleRequestById sql) {
            logger.instance().error("SQL error during query reqest of schedule by id in post method: " + sql.getMessage());
            sql.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        } catch (ScheduleExistsException sche) {
            logger.instance().error("Schedule with same name exists already!");
            sche.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Schedule with this name exists already");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            Integer scheduleId = Integer.parseInt(req.getParameter("scheduleId"));

            logger.instance().info("ScheduleServlet: delete request, shchedule id recieved: " + scheduleId);

            ScheduleService scheduleService = new ScheduleService(connection);
            scheduleService.deleteSchedule(scheduleId);

            logger.instance().info("ScheduleServlet: delete successful, response sent with HTTP OK");
            sendTextMessage(resp,HttpServletResponse.SC_OK,"ok");

        } catch (SQLException e) {
            logger.instance().error("SQL Exception in ScheduleServlet delete method: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp,HttpServletResponse.SC_NO_CONTENT,"No schedule with this id");
        }
    }
}
