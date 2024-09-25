package me.krutikov.schedule.parser;

import me.krutikov.schedule.data.ScheduleDay;
import org.jsoup.nodes.Element;

public interface ScheduleParser {

    ScheduleDay parse(Element day);

}
