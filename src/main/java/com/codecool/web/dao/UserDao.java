package com.codecool.web.dao;


import com.codecool.web.model.Role;
import com.codecool.web.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class UserDao extends AbstractDao implements IUserDao {


    public UserDao(Connection connection) {
        super(connection);
    }

    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(assembleUser(resultSet));
                }
            }
        return users;
        }
    }

    public User findUserByEmail(String email) throws SQLException{
        String sql = "SELECT * FROM users WHERE email=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, email);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return assembleUser(resultSet);
                }
            }
            return null;
        }
    }

    public void addNewUser(String email, String name) throws SQLException {
        String sql = "INSERT INTO users(email, username, userrole) VALUES (?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, "USER");
            preparedStatement.executeUpdate();
        }
    }

    public User assembleUser(ResultSet resultSet) throws SQLException, IllegalArgumentException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String name = resultSet.getString("username");
        String password = resultSet.getString("password");
        Role userRole = Role.valueOf(resultSet.getString("userrole"));
        return new User(id, email, name, password, userRole);
    }

}
