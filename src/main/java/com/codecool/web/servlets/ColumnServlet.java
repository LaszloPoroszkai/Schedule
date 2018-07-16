package com.codecool.web.servlets;

import com.codecool.web.dao.exceptions.MaxColumnsException;
import com.codecool.web.model.Column;
import com.codecool.web.services.ColumnService;
import com.codecool.web.services.EventService;
import com.codecool.web.services.ScheduleService;
import com.codecool.web.services.TasksService;
import com.codecool.web.services.exceptions.InvalidUserException;
import com.codecool.web.services.exceptions.IvalidUserIdException;
import com.codecool.web.services.exceptions.SQLErrorDuringScheduleRequestById;
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


@WebServlet("/protected/columns")
public class ColumnServlet extends AbstractServlet {
    private final Logging logger = new Logging(ColumnServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = getConnection(req.getServletContext())) {

            if (req.getParameter("schId") != null) {
                Integer schId = Integer.parseInt(req.getParameter("schId"));
                ColumnService columnSer = new ColumnService(connection);

                List<Column> columns = columnSer.fetchColumnsBySchedId(schId);

                if (columns.size() == 0) {
                    List<Integer> schid = new ArrayList<>();
                    schid.add(schId);
                    sendMessage(resp, HttpServletResponse.SC_FOUND, schid);
                } else {
                    sendMessage(resp, HttpServletResponse.SC_FOUND, columns);
                }
            }else if (req.getParameter("columnId") != null) {
                Integer columnId = Integer.parseInt(req.getParameter("columnId"));
                ColumnService columnSer = new ColumnService(connection);

                List<Column> columns = columnSer.fetchColumnsByColumnId(columnId);

                sendMessage(resp, HttpServletResponse.SC_FOUND, columns);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HTTP ERROR 500 -- Database error");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {

            Integer schid = Integer.parseInt(req.getParameter("schid"));
            String title = req.getParameter("title");

            ColumnService columnser = new ColumnService(connection);
            List<Column> columns = columnser.addColumn(schid, title);

            sendMessage(resp, HttpServletResponse.SC_FOUND, columns);

        } catch (SQLException e) {
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error.");
        } catch (MaxColumnsException m) {
            m.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "an not add more than 7 columns");
        }
    }
}
