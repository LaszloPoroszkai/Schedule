package com.codecool.web.dao;


import com.codecool.web.dao.exceptions.MaxColumnsException;
import com.codecool.web.model.Column;
import com.codecool.web.model.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColumnDao extends AbstractDao implements IColumnDao {
    public ColumnDao(Connection connection) {
        super(connection);
    }

    public List<Column> fetchColumnsBySchedId(int schId) throws SQLException {
        List<Column> result = new ArrayList<>();
        String sql = "SELECT * FROM columns WHERE schid=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, schId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    result.add(assembleColumn(resultSet));
                }
            }
        }
        return result;
    }

    public List<Column> fetchColumnsByColumnId(Integer colId) throws SQLException {
        List<Column> result = new ArrayList<>();
        String sql = "SELECT * FROM columns WHERE schid in (SELECT schid FROM columns WHERE id=?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, colId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    result.add(assembleColumn(resultSet));
                }
            }
        }
        return result;
    }

    public void addNewColumn(int schid, String title) throws SQLException, MaxColumnsException {
        String sql = "INSERT INTO columns (title, schid) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            if (fetchColumnsBySchedId(schid).size()>=7) {
                throw new MaxColumnsException();
            }else{
                preparedStatement.setString(1, title);
                preparedStatement.setInt(2, schid);
                preparedStatement.executeUpdate();
            }
        }
    }


    private Column assembleColumn(ResultSet resultSet) throws SQLException {
        String title = resultSet.getString("title");
        int id = resultSet.getInt("id");
        int schId = resultSet.getInt("schid");
        return new Column(id, schId, title);

    }
}
