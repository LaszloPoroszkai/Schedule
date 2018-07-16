package com.codecool.web.servlets;

import com.codecool.web.util.Logging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/protected/logout")
public class LogoutServlet extends AbstractServlet {
    private final Logging logger = new Logging(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getSession().invalidate();
            logger.instance().info("Successful logout");
            sendTextMessage(resp, HttpServletResponse.SC_RESET_CONTENT, "Successful logout");
        }
        catch (IOException i){
            logger.instance().error("IOException: " + i.getMessage());
            i.printStackTrace();
            throw new IOException(i);
        }
    }
}
