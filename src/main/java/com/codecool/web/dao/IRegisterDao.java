package com.codecool.web.dao;

import com.codecool.web.dao.exceptions.EmailAlreadyExistException;
import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.dao.exceptions.UserNameAlreadyExistException;
import com.codecool.web.model.Role;

import java.sql.SQLException;

public interface IRegisterDao {
    void isEmailExist(String email) throws SQLException, EmailAlreadyExistException;
    void isNameExist(String name) throws UserNameAlreadyExistException, SQLException;
    void registerUser(String email, String name, String password, Role role) throws SQLException, SQLErrorDuringQueryRequestException;


}
