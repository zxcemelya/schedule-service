package me.krutikov.schedule.parser;

import lombok.val;
import me.krutikov.schedule.data.Lesson;
import me.krutikov.schedule.data.ScheduleDay;
import org.jsoup.nodes.Element;

import java.time.LocalTime;
import java.util.ArrayList;

public class UunitScheduleParser implements ScheduleParser {

    @Override
    public ScheduleDay parse(Element day) {
        ScheduleDay schedule;
        try {
            val eDate = day.select("h2.date").text();
            val eLessons = day.select("li.lesson");

            val lessons = new ArrayList<Lesson>();

            for (val lesson : eLessons) {
                val number = lesson.select(".number").text();
                if (number.isEmpty()) {
                    continue;
                }

                val prep = lesson.select(".prep").text();
                val time = lesson.select(".time").text();
                val name = lesson.select(".name").text();
                val audience = lesson.select(".cab").text();

                val timeParts = time.split(" - ");

                lessons.add(Lesson.builder()
                        .number(Integer.parseInt(number.replaceAll("\\D", "")))
                        .audience(audience)
                        .name(name)
                        .teacher(prep)
                        .startTime(LocalTime.parse(timeParts[0]))
                        .endTime(LocalTime.parse(timeParts[1]))
                        .build()
                );
            }

            schedule = ScheduleDay.builder()
                    .date(eDate.split(" ")[1])
                    .lessons(lessons)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return schedule;
    }
}
