package com.codecool.web.dao.exceptions;

import java.sql.SQLException;

public class SQLErrorDuringQueryRequestException extends Throwable {
    public SQLErrorDuringQueryRequestException(SQLException s) {
    }
}
