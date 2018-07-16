package com.codecool.web.servlets;

import com.codecool.web.dao.exceptions.EmailAlreadyExistException;
import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.dao.exceptions.UserNameAlreadyExistException;
import com.codecool.web.services.IRegisterService;
import com.codecool.web.services.RegisterService;
import com.codecool.web.services.exceptions.InputIsEmptyException;
import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/register")
public class RegisterServlet extends AbstractServlet {
    private static final Logging logger = new Logging(RegisterServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            String password = req.getParameter("password");

            logger.instance().info("Attempt to register with email: " + email);
            logger.instance().info("Attempt to register with name: " + name);
            logger.instance().info("Attempt to register with password: " + password);

            IRegisterService registerService = new RegisterService(connection);
            registerService.isAccountAvailable(email, name);
            registerService.registerUser(email, name, password);

            logger.instance().info("Registration successful");
            sendTextMessage(resp, HttpServletResponse.SC_OK, "Registration successful");

        } catch (SQLException e) {
            logger.instance().error("SQL exception: " + e.getMessage());
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HTTP ERROR 500, Please contact with the administrator");
        } catch (EmailAlreadyExistException e) {
            logger.instance().info("Registration failed, email aready exists");
            sendTextMessage(resp, HttpServletResponse.SC_CONFLICT, "This email address is already taken");
        } catch (UserNameAlreadyExistException e) {
            logger.instance().info("Registration failed, user name aready exists");
            sendTextMessage(resp, HttpServletResponse.SC_CONFLICT, "This user name is already taken");
        } catch (SQLErrorDuringQueryRequestException ee) {
            logger.instance().error("SQL query is invalid: " + ee.getMessage());
            ee.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HTTP ERROR 500, Please contact with the administrator");
        } catch (InputIsEmptyException e) {
            logger.instance().warn("No input information got");
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "Please fill all the fields to register");
        }
    }
}
