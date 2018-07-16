package com.codecool.web.dao;

import com.codecool.web.model.Column;

import java.sql.SQLException;
import java.util.List;

public interface IColumnDao {
    List<Column> fetchColumnsBySchedId(int id) throws SQLException;
    List<Column> fetchColumnsByColumnId(Integer id) throws SQLException;
}
