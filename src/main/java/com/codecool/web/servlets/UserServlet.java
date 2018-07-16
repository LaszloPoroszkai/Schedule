package com.codecool.web.servlets;

import com.codecool.web.model.User;
import com.codecool.web.services.IUserService;
import com.codecool.web.services.UserService;
import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/users")
public class UserServlet extends AbstractServlet {
    private final Logging logger = new Logging(UserServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())) {
            IUserService userService = new UserService(connection);
            List<User> users = userService.getAllUsers();
            logger.instance().info("All user information has been sent back to the client");
            sendMessage(resp, HttpServletResponse.SC_OK, users);

        } catch (SQLException e) {
            logger.instance().error("SQL Exception " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
