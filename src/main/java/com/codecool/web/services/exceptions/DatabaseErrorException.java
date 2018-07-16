package com.codecool.web.services.exceptions;

import java.sql.SQLException;

public class DatabaseErrorException extends Exception {
    public DatabaseErrorException(SQLException e) {
        super(e);
    }
}
