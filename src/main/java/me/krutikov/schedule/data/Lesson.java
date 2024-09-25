package me.krutikov.schedule.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Lesson {

    int number;

    LocalTime startTime;
    LocalTime endTime;

    String name;
    String teacher;
    String audience;
}
