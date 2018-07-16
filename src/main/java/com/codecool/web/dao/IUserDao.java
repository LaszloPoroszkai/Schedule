package com.codecool.web.dao;

import com.codecool.web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    List<User> findAllUsers() throws SQLException;
    User findUserByEmail(String email) throws SQLException;
    void addNewUser(String email, String name) throws SQLException;
    User assembleUser(ResultSet resultSet) throws SQLException;
}
