package com.codecool.web.servlets;

import com.codecool.web.model.User;
import com.codecool.web.services.IUserService;
import com.codecool.web.services.UserService;
import com.codecool.web.services.exceptions.DatabaseErrorException;
import com.codecool.web.services.exceptions.InvalidUserException;
import com.codecool.web.util.Logging;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends AbstractServlet{
    private static final Logging logger = new Logging(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {


            String email = req.getParameter("email");
            String password = req.getParameter("password");

            logger.instance().info("Attempt to login with email: " + email);
            logger.instance().info("Attempt to login with password: " + password);


            IUserService userService = new UserService(connection);
            User user = userService.loginUser(email, password);

            req.getSession().setAttribute("user", user);
            logger.instance().info("Login successful with email: " + email);

            sendMessage(resp, HttpServletResponse.SC_FOUND, user);

        } catch (InvalidUserException na){
            logger.instance().info("Login attempt failed, wrong email or password");
            sendTextMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, na.getMessage());
        } catch (IllegalArgumentException ill){
            logger.instance().error("Illegal argument: " + ill.getMessage());
            ill.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to parse the user role");
        } catch (DatabaseErrorException de) {
            logger.instance().error("Database error: " + de.getMessage());
            de.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (SQLException e) {
            logger.instance().error("SQL error: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query error");
        } catch (IOException io){
            logger.instance().error("IOException happened: " + io.getMessage());
            io.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

}
