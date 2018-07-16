package com.codecool.web.servlets;

import com.codecool.web.model.User;
import com.codecool.web.services.GVerifyService;
import com.codecool.web.services.UserService;
import com.codecool.web.services.exceptions.DatabaseErrorException;
import com.codecool.web.services.exceptions.InvalidTokenException;
import com.codecool.web.services.exceptions.UserNotRegisteredException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/glogin")
public class GLoginServlet extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token_id = req.getParameter("idtoken");

        GVerifyService gvf = new GVerifyService();
        try {
            String email = gvf.VerifyToken(token_id);
            if (email != null) {
                String userName = gvf.getUserName(token_id);
                System.out.println(userName);
                try (Connection connection = getConnection(req.getServletContext())) {
                    UserService us = new UserService(connection);
                    User user = us.getGoogleUser(email, userName);

                    req.getSession().setAttribute("user", user);

                    sendMessage(resp, HttpServletResponse.SC_FOUND, user);
                }
            }
        }catch (GeneralSecurityException gse) {
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, gse.getMessage());
        }catch (IOException ioe) {
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, ioe.getMessage());
        }catch (InvalidTokenException ite) {
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, ite.getMessage());
        }catch (SQLException sqe) {
            sqe.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, sqe.getMessage());
        }catch (UserNotRegisteredException une) {
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, une.getMessage());
        }catch (DatabaseErrorException dee) {
            sendTextMessage(resp, HttpServletResponse.SC_BAD_REQUEST, dee.getMessage());
        }
    }
}