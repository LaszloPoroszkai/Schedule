package com.codecool.web.services;

import com.codecool.web.model.Schedule;
import com.codecool.web.services.exceptions.SQLErrorDuringScheduleRequestById;

import java.util.List;

public interface IScheduleService {
    List<Schedule> findSchedulesByUserId(int id) throws SQLErrorDuringScheduleRequestById;
}
