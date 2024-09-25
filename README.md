## Подключение Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.zxcemelya</groupId>
    <artifactId>schedule-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 💡 Пример использования

```java
   public static void main(String[] args) {
        ScheduleService scheduleService = new UunitScheduleService();
        ScheduleWeek schedule = scheduleService.getSchedule("К-4СИС31"); //К -> Колледж
        for (ScheduleDay day : schedule.getScheduleDays()) {
            System.out.println(day.getDate());
            for (Lesson lesson : day.getLessons()) {
                System.out.println(lesson.getName());
            }
        }
    }
```
