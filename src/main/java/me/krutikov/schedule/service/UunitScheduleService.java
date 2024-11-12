package me.krutikov.schedule.service;

import lombok.val;
import me.krutikov.schedule.data.ScheduleDay;
import me.krutikov.schedule.data.ScheduleWeek;
import me.krutikov.schedule.parser.ScheduleParser;
import me.krutikov.schedule.parser.UunitScheduleParser;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UunitScheduleService implements ScheduleService {

    private static final String GROUP_ID_URL = "http://edu.struust.ru/php/getIdGroup.php";
    private static final String SCHEDULE_URL = "http://edu.struust.ru/php/getShedule.php";

    private final ScheduleParser scheduleParser = new UunitScheduleParser();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public ScheduleWeek getSchedule(final String group) {
        return getSchedule(group, 0);
    }

    @Override
    public ScheduleWeek getSchedule(final String group, final int week) {
        val request = HttpRequest.newBuilder()
                .uri(URI.create(SCHEDULE_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("type=2&id=%s&week=%s".formatted(
                        getGroupId(group), week
                )))
                .build();

        String rawSchedule;
        try {
            rawSchedule = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
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
        String params = "group_name=" + URLEncoder.encode(group, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROUP_ID_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();
        try {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (Exception e) {
            return 0;
        }
    }
}
