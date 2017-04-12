package com.moodlogger.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {

    public static Calendar getDateFromSqliteDateTime(String dateTime) {
        try {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dateFormat.parse(dateTime);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            Log.e("ERROR", "Error occurred parsing mood entry date to java format", e);
            throw new RuntimeException(e);
        }
    }

    public static String getSqliteDateForQuery(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getStartOfWeek() {
        Calendar calendar = getCalendarForNow();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        setTimeToBeginningOfDay(calendar);
        return calendar;
    }

    public static Calendar getEndOfWeek() {
        Calendar calendar = getStartOfWeek();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return calendar;
    }

    public static Calendar getStartOfMonth() {
        return getStartOfPeriod(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getEndOfMonth() {
        return getEndOfPeriod(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getStartOfYear() {
        return getStartOfPeriod(Calendar.DAY_OF_YEAR);
    }

    public static Calendar getEndOfYear() {
        return getEndOfPeriod(Calendar.DAY_OF_YEAR);
    }

    private static Calendar getStartOfPeriod(int period) {
        Calendar calendar = getCalendarForNow();
        calendar.set(period, calendar.getActualMinimum(period));
        setTimeToBeginningOfDay(calendar);
        return calendar;
    }

    private static Calendar getEndOfPeriod(int period) {
        Calendar calendar = getCalendarForNow();
        calendar.set(period, calendar.getActualMaximum(period));
        setTimeToEndofDay(calendar);
        return calendar;
    }

    private static Calendar getCalendarForNow() {
        return GregorianCalendar.getInstance();
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
