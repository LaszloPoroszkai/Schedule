package com.codecool.web.servlets;

import com.codecool.web.model.Event;
import com.codecool.web.model.Schedule;
import com.codecool.web.services.EventService;
import com.codecool.web.services.exceptions.EventsOverlapException;
import com.codecool.web.services.exceptions.TaskAddedAlreadyException;
import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/events")
public class EventsServlet extends AbstractServlet {
    private final Logging logger = new Logging(EventsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            if(req.getParameter("schid") != null) {
                Integer schid = Integer.parseInt(req.getParameter("schid"));
                EventService eventser = new EventService(connection);

                logger.instance().info("EventsServlet: get request schid recieved: " + schid);

                List<Event> events = eventser.getEventsBySchid(schid);

                logger.instance().info("Response sent from EventsServlet with HTTP status FOUND");
                sendMessage(resp, HttpServletResponse.SC_FOUND, events);
            }else if(req.getParameter("id") != null) {
                Integer id = Integer.parseInt(req.getParameter("id"));
                EventService eventser = new EventService(connection);

                logger.instance().info("EventsServlet: get request id recieved: " + id);

                Event event = eventser.getEventById(id);

                if (event != null) {
                    logger.instance().info("Response sent from EventsServlet with HTTP status FOUND");
                    sendMessage(resp, HttpServletResponse.SC_FOUND, event);
                }else{
                    logger.instance().info("Response sent from EventsServlet with HTTP status NOT FOUND");
                    sendMessage(resp, HttpServletResponse.SC_NOT_FOUND, event);
                }
            }
        } catch (SQLException e) {
            logger.instance().error("SQL Exception in EventsServlet get method: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer taskid = Integer.parseInt(req.getParameter("taskid"));
            Integer columnid = Integer.parseInt(req.getParameter("columnid"));
            Integer starttime = Integer.parseInt(req.getParameter("starttime"));
            Integer endtime = Integer.parseInt(req.getParameter("endtime"));
            String description = req.getParameter("desc");

            logger.instance().info("EventsServlet: post request taskid recieved: " + taskid);
            logger.instance().info("EventsServlet: post request columnid recieved: " + columnid);
            logger.instance().info("EventsServlet: post request stat time recieved: " + starttime);
            logger.instance().info("EventsServlet: post request end time recieved: " + endtime);
            logger.instance().info("EventsServlet: post request description recieved: " + description);

            EventService eventser = new EventService(connection);
            if(starttime > endtime || starttime.equals(endtime)) {
                logger.instance().warn("Response sent from EventsServlet: Start time can not be greater or equal to end time!");
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Start time can not be greater or equal to end time!");
            }else if(0>starttime||starttime>23||endtime>24||endtime<1){
                logger.instance().warn("Response sent from EventsServlet: Not a valid time setting");
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not a valid time setting!");
            }else{
                List<Schedule> schid = eventser.addEvent(taskid, columnid, starttime, endtime, description);
                logger.instance().info("Response sent from EventsServlet with HTTP status FOUND");

                sendMessage(resp, HttpServletResponse.SC_FOUND, schid);
            }
        } catch (SQLException e) {
            logger.instance().error("SQL Exception in EventsServlet post method: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error.");
        } catch (EventsOverlapException o) {
            logger.instance().error("Event Overlap Exception in EventsServlet post method: " + o.getMessage());
            o.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Events can not overlap existing events.");
        } catch (TaskAddedAlreadyException t) {
            logger.instance().error("Task already added exception in EventsServlet post method: " + t.getMessage());
            t.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Task already exists in this schedule.");
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer id = Integer.parseInt(req.getParameter("id"));
            Integer taskid = Integer.parseInt(req.getParameter("taskid"));
            Integer columnid = Integer.parseInt(req.getParameter("columnid"));
            Integer starttime = Integer.parseInt(req.getParameter("start"));
            Integer endtime = Integer.parseInt(req.getParameter("end"));
            String description = req.getParameter("desc");

            logger.instance().info("EventsServlet: put request eventid recieved: " + id);
            logger.instance().info("EventsServlet: put request taskid recieved: " + taskid);
            logger.instance().info("EventsServlet: put request columnid recieved: " + columnid);
            logger.instance().info("EventsServlet: put request stat time recieved: " + starttime);
            logger.instance().info("EventsServlet: put request end time recieved: " + endtime);
            logger.instance().info("EventsServlet: put request description recieved: " + description);

            EventService eventser = new EventService(connection);
            if(starttime > endtime || starttime.equals(endtime)) {
                logger.instance().warn("Response sent from EventsServlet: Start time can not be greater or equal to end time!");
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Start time can not be greater or equal to end time!");
            }else if(0>starttime||starttime>23||endtime>24||endtime<1){
                logger.instance().warn("Response sent from EventsServlet: Not a valid time setting");
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not a valid time setting!");
            }else{
                List<Schedule> schid = eventser.updateEvent(id, taskid, columnid, starttime, endtime, description);
                logger.instance().info("Response sent from EventsServlet with HTTP status FOUND");

                sendMessage(resp, HttpServletResponse.SC_FOUND, schid);
            }
        } catch (SQLException e) {
            logger.instance().error("SQL Exception in EventsServlet post method: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error.");
        } catch (EventsOverlapException o) {
            logger.instance().error("Event Overlap Exception in EventsServlet post method: " + o.getMessage());
            o.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Events can not overlap existing events.");
        } catch (TaskAddedAlreadyException t) {
            logger.instance().error("Task already added exception in EventsServlet post method: " + t.getMessage());
            t.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Task already exists in this schedule.");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer id = Integer.parseInt(req.getParameter("id"));
            Integer columnid = Integer.parseInt(req.getParameter("columnid"));

            logger.instance().info("EventsServlet: delete request eventid recieved: " + id);
            logger.instance().info("EventsServlet: delete request columnid recieved: " + columnid);

            EventService eventser = new EventService(connection);

            List<Schedule> schid = eventser.deleteEvent(id, columnid);
            logger.instance().info("Response sent from EventsServlet with HTTP status FOUND");

            sendMessage(resp, HttpServletResponse.SC_FOUND, schid);

        } catch (SQLException e) {
            logger.instance().error("SQL Exception in EventsServlet post method: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error.");
        }
    }
}