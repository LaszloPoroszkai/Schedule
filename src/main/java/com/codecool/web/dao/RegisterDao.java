package com.codecool.web.dao;

import com.codecool.web.dao.exceptions.EmailAlreadyExistException;
import com.codecool.web.dao.exceptions.SQLErrorDuringQueryRequestException;
import com.codecool.web.dao.exceptions.UserNameAlreadyExistException;
import com.codecool.web.model.Role;

import java.sql.*;

public class RegisterDao extends AbstractDao implements IRegisterDao {

    public RegisterDao(Connection connection) {
        super(connection);
    }

    @Override
    public void isEmailExist(String email) throws SQLException, EmailAlreadyExistException {
        String sql = "SELECT id FROM users WHERE email=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                   throw new EmailAlreadyExistException();
                }
            }
        }

    }

    @Override
    public void isNameExist(String name) throws UserNameAlreadyExistException, SQLException {
        String sql = "SELECT id FROM users WHERE username=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    throw new UserNameAlreadyExistException();
                }
            }
        }
    }

    @Override
    public void registerUser(String email, String name, String password, Role role) throws SQLException, SQLErrorDuringQueryRequestException {
        String sql = "INSERT INTO users(email, username, password, userrole) VALUES (?, ?, ?, ?);";
        connection.setAutoCommit(false);
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role.name());
            executeInsert(preparedStatement);
        }
        catch (SQLException s){
            connection.rollback();
            throw new SQLErrorDuringQueryRequestException(s);
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
