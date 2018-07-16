package com.codecool.web.services;


import com.codecool.web.dao.IUserDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.model.User;
import com.codecool.web.services.exceptions.DatabaseErrorException;
import com.codecool.web.services.exceptions.InvalidUserException;
import com.codecool.web.services.exceptions.UserNotRegisteredException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class UserService implements IUserService {
    private final IUserDao userDao;


    public UserService(Connection connection) {
        this.userDao = new UserDao(connection);
    }

    public User loginUser(String emailInput, String passwordInput) throws InvalidUserException, IllegalArgumentException, DatabaseErrorException {
        List<User> registeredUsers = null;
        User currentLogin = new User(emailInput, passwordInput);
        try {
            registeredUsers = userDao.findAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseErrorException(e);
        }
        for (User usr: registeredUsers) {
            if(usr.equals(currentLogin)){
                return usr;

            }
        }
        throw new InvalidUserException("Wrong user name or password");
    }

    public User getGoogleUser(String email, String userName) throws UserNotRegisteredException, IllegalArgumentException, DatabaseErrorException {
        try {
            User user = userDao.findUserByEmail(email);
            if (user!=null) {
                return user;
            }else{
                userDao.addNewUser(email, userName);
                return userDao.findUserByEmail(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseErrorException(e);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.findAllUsers();
    }

}
