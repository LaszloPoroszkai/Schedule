package com.codecool.web.services;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.exceptions.MaxColumnsException;
import com.codecool.web.model.Column;
import com.codecool.web.services.exceptions.IvalidUserIdException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColumnService implements IColumnService {
    private final ColumnDao columnDao;

    public ColumnService(Connection connection) {
        columnDao = new ColumnDao(connection);
    }

    public List<Column> fetchColumnsBySchedId(Integer scheduleId) {
        List<Column> result = new ArrayList<>();
        try{
            int schId = scheduleId;
            result = columnDao.fetchColumnsBySchedId(schId);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Column> fetchColumnsByColumnId(Integer colId) {
        List<Column> result = new ArrayList<>();
        try{
            result = columnDao.fetchColumnsByColumnId(colId);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Column> addColumn(Integer schid, String title) throws MaxColumnsException {
        List<Column> columns = new ArrayList<>();
        try{
            if(columnDao.fetchColumnsBySchedId(schid).size() >= 7) {
                throw new MaxColumnsException();
            }else {
                columnDao.addNewColumn(schid, title);
                columns = columnDao.fetchColumnsBySchedId(schid);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return columns;
    }
}
