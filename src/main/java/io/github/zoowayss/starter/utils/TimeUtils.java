package io.github.zoowayss.starter.utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class TimeUtils {

    public static Long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    // 获取ds日期对应的时间戳（单位：秒）
    public static Long GetDsToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = simpleDateFormat.parse(s);
            return date.getTime() / 1000;
        } catch (Exception e) {
            return 0L;
        }
    }

    // 获取当前毫秒时间戳
    public static Long getCurrentTimeMils() {
        return Instant.now().toEpochMilli();
    }

    // 获取当天某个时间点的时间戳（精确到毫秒）
    public static Long getTodayTimeStamp(Integer hour, Integer minute, Integer second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    // 获取当天某个时间点的时间戳（精确到毫秒）
    public static Long getDayTimeStamp(Integer day, Integer hour, Integer minute, Integer second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    // 获取多少年以后的时间戳
    public static Long getYearTimeStamp(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, year);
        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    // 根据时间戳获取日期字符串
    public static String getDataStr(Long timestamp, String pattern) {
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(new Date(timestamp));
    }

    public static String getDs() {
        String ds = TimeUtils.timeFormatWithUnixTimestampAndFormat(getCurrentTimeStamp() - 86400, "");
        return ds;
    }

    public static String timeFormatWithUnixTimestampAndFormat(Long tm, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyyMMdd";
        }
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(format);

        return Instant.ofEpochSecond(tm)
                      .atZone(ZoneId.of("GMT+8"))
                      .format(formatter);
    }

    public static Integer getWeekNumberOf(String inStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");   // Define formatting pattern to match your input string./yy");   // Define formatting pattern to match your input string.
        LocalDate date = LocalDate.parse(inStr, formatter);                     // Parse string into a `LocalDate` object.

        WeekFields wf = WeekFields.of(
                Locale.getDefault());                    // Use week fields appropriate to your locale. People in different places define a week and week-number differently, such as starting on a Monday or a Sunday, and so on.
        TemporalField weekNum = wf.weekOfWeekBasedYear();                       // Represent the idea of this locale’s definition of week number as a `TemporalField`.
        int week = Integer.parseInt(String.format("%02d", date.get(weekNum)));   // Using that locale’s definition of week number, determine the week-number for this particular `LocalDate` value.

        return week;
    }

    public static String timeInMilsFormatWithUnixTimestampAndFormat(Long tm, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if (tm == null) {
            return "";
        }
        if (tm <= 0) {
            return "";
        }
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(format);

        return Instant.ofEpochMilli(tm)
                      .atZone(ZoneId.of("GMT+8"))
                      .format(formatter);
    }

    public static Long getDayZeroTimestampMils() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getLastDayZeroTimestampMils() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getNextDayZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setTimeInMillis(tm);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getHourZeroTimestampMils() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    public static Long getMinutesZeroTimestampMils() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getDayZeroTimestampSecond() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    public static Long getWeekZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getLastWeekZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getNextWeekZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getMonthZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getLastMonthZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getNextMonthZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(tm);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getYearZeroTimestampMils(long tm) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    public static DayOfWeek getDayOfWeek(Long tm) {

        return Instant.ofEpochMilli(tm).atZone(ZoneId.of("GMT+8")).getDayOfWeek();
    }

    public static Integer getHour() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String getCurrentDay() {
        return TimeUtils.timeFormatWithUnixTimestampAndFormat(getCurrentTimeStamp(), "yyyy-MM-dd");
    }

    public static Long getZeroTimeMilsOf(double vf) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        cal.setTimeInMillis((long) vf);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getMilsTillNextZero() {

        Long ts = getDayZeroTimestampMils();
        Long now = getCurrentTimeMils();

        return (86400000L - (now - ts));
    }

    public static Long getSecondsTillNextZero() {

        Long ts = getDayZeroTimestampSecond();
        Long now = getCurrentTimeStamp();

        return (86400L - (now - ts));
    }

}
