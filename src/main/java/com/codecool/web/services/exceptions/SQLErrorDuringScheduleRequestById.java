package com.codecool.web.services.exceptions;

import java.sql.SQLException;

public class SQLErrorDuringScheduleRequestById extends Throwable {
    public SQLErrorDuringScheduleRequestById(SQLException e) {
    }
}
