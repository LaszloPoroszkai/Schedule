package com.codecool.web.services;

import com.codecool.web.dao.IRegisterDao;
import com.codecool.web.dao.RegisterDao;
import com.codecool.web.dao.exceptions.EmailAlreadyExistException;
import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.dao.exceptions.UserNameAlreadyExistException;
import com.codecool.web.model.Role;
import com.codecool.web.services.exceptions.InputIsEmptyException;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterService implements IRegisterService {
    private final IRegisterDao registerDao;
    private Role DEFAULT_ROLE = Role.USER;

    public RegisterService(Connection connection) {
        registerDao = new RegisterDao(connection);
    }

    @Override
    public void isAccountAvailable(String email, String name) throws SQLException, EmailAlreadyExistException, UserNameAlreadyExistException {
        registerDao.isEmailExist(email);
        registerDao.isNameExist(name);
    }

    @Override
    public void registerUser(String email, String name, String password) throws SQLException, SQLErrorDuringQueryRequestException, InputIsEmptyException {
        if(isInputDataNotEmpty(email, name, password)) {
            registerDao.registerUser(email, name, password, DEFAULT_ROLE);
        }

    }

    public boolean isInputDataNotEmpty(String email, String name, String password) throws InputIsEmptyException {
        if(email.equals("") || name.equals("") || password.equals("")){
            throw new InputIsEmptyException();
        }
        return true;
    }
}
