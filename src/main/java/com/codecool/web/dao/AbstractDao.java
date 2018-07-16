package com.codecool.web.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AbstractDao extends HttpServlet {

    final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    void executeInsert(PreparedStatement preparedStatement) throws SQLException {
        int insertCount = preparedStatement.executeUpdate();
        if (insertCount != 1) {
            connection.rollback();
            throw new SQLException("Failed to insert 1 row");
        }
    }

}
