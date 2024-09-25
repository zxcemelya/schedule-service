package me.krutikov.schedule.service;

import lombok.val;
import me.krutikov.schedule.data.ScheduleDay;
import me.krutikov.schedule.data.ScheduleWeek;
import me.krutikov.schedule.parser.ScheduleParser;
import me.krutikov.schedule.parser.UunitScheduleParser;
import me.krutikov.schedule.utils.RequestUtils;
import org.jsoup.Jsoup;

import java.util.Optional;

public class UunitScheduleService implements ScheduleService {

    private static final String GROUP_ID_URL = "http://edu.strbsu.ru/php/getIdGroup.php";
    private static final String SCHEDULE_URL = "http://edu.strbsu.ru/php/getShedule.php";

    private final ScheduleParser scheduleParser = new UunitScheduleParser();

    @Override
    public ScheduleWeek getSchedule(final String group) {
        return getSchedule(group, 0);
    }

    @Override
    public ScheduleWeek getSchedule(final String group, final int week) {
        String rawSchedule;
        try {
            rawSchedule = RequestUtils.postRequest(SCHEDULE_URL, String.format("type=2&id=%s&week=%s", getGroupId(group), week));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        val doc = Jsoup.parse(rawSchedule);
        val result = doc.select(".day").stream()
                .map(scheduleParser::parse)
                .toList();

        if (result.isEmpty()) {
            throw new RuntimeException("No schedule found for group " + group + ", week " + week);
        }

        return ScheduleWeek.builder()
                .scheduleDays(result)
                .build();
    }

    @Override
    public Optional<ScheduleDay> getScheduleByDate(final String group, final String date) {
        return getSchedule(group).getScheduleDays().stream()
                .filter(schedule -> schedule.getDate().equals(date))
                .findFirst();
    }

    @Override
    public int getGroupId(final String group) {
        try {
            return Integer.parseInt(RequestUtils.postRequest(GROUP_ID_URL, "group_name=" + group));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
