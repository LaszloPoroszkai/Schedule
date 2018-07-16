package com.codecool.web.services;

import com.codecool.web.model.Column;
import com.codecool.web.services.exceptions.IvalidUserIdException;

import java.util.List;

public interface IColumnService {
    List<Column> fetchColumnsBySchedId(Integer scheduleId) throws IvalidUserIdException;
}
