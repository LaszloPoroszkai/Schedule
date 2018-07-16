package com.codecool.web.services;


import com.codecool.web.model.User;
import com.codecool.web.services.exceptions.DatabaseErrorException;
import com.codecool.web.services.exceptions.InvalidUserException;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    User loginUser(String emailInput, String passwordInput) throws InvalidUserException, DatabaseErrorException;
    public List<User> getAllUsers() throws SQLException;
}
