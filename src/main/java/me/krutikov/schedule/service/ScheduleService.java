package me.krutikov.schedule.service;

import me.krutikov.schedule.data.ScheduleDay;
import me.krutikov.schedule.data.ScheduleWeek;

import java.util.Optional;

public interface ScheduleService {

    ScheduleWeek getSchedule(String group);
    ScheduleWeek getSchedule(String group, int week);
    Optional<ScheduleDay> getScheduleByDate(String group, String date);

    int getGroupId(String group);

}
