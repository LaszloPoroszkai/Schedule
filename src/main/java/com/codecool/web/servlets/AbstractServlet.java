package com.codecool.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.codecool.web.dto.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    Connection getConnection(ServletContext servletContext) throws SQLException {
        DataSource dataSource = (DataSource) servletContext.getAttribute("scheduleMaster");
        return dataSource.getConnection();
    }

    void sendMessage(HttpServletResponse resp, int status, Object object) throws IOException {
        resp.setStatus(status);
        try {
            objectMapper.writeValue(resp.getOutputStream(), object);

        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e);
        }
    }

    void sendTextMessage(HttpServletResponse resp, int status, String message) throws IOException {
        sendMessage(resp, status, new MessageDto(message));
    }

    String fetchRequestUrl(HttpServletRequest req){
        StringBuffer requestURL = req.getRequestURL();
        if (req.getQueryString() != null) {
            requestURL.append("?").append(req.getQueryString());
        }
        return requestURL.toString();
    }

    String fetchUserIdFromUrl(String url){
        String pattern = "(?:id=)(\\d+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher m = regex.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        return "*";
    }

}
