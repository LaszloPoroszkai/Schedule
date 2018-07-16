package com.codecool.web.services;

import com.codecool.web.dao.exceptions.EmailAlreadyExistException;
import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.dao.exceptions.UserNameAlreadyExistException;
import com.codecool.web.services.exceptions.InputIsEmptyException;

import java.sql.SQLException;

public interface IRegisterService {
    void isAccountAvailable(String email, String name) throws SQLException, EmailAlreadyExistException, UserNameAlreadyExistException;
    void registerUser(String email, String name, String password) throws SQLException, SQLErrorDuringQueryRequestException, InputIsEmptyException;
    boolean isInputDataNotEmpty(String email, String name, String password) throws InputIsEmptyException;
}
