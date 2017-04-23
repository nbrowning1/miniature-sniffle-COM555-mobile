package com.moodlogger.utils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void itGetsDateFromSqliteDateTimeFormat() {
        String testSqliteDateTime = "2010-01-31 12:30:05";
        Calendar calendar = DateUtils.getDateFromSqliteDateTime(testSqliteDateTime);

        assertEquals(calendar.get(Calendar.YEAR), 2010);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.JANUARY);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 31);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
        assertEquals(calendar.get(Calendar.MINUTE), 30);
        assertEquals(calendar.get(Calendar.SECOND), 5);
    }

    @Test
    public void itGetsSqliteDateTimeFormatForDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2012);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 30);

        String expectedSqliteDateTime = "2012-12-25 17:15:30";
        assertEquals(DateUtils.getSqliteDateForQuery(calendar), expectedSqliteDateTime);
    }

    @Test
    public void itGetsStartOfWeek() {
        Calendar calendar = DateUtils.getStartOfWeek();
        testCalendarIsBeginningOfWeek(calendar);

        Calendar now = Calendar.getInstance();
        // start of this week
        assertTrue(now.after(calendar));
    }

    @Test
    public void itGetsEndOfWeek() {
        Calendar calendar = DateUtils.getEndOfWeek();
        testCalendarIsBeginningOfWeek(calendar);

        Calendar now = Calendar.getInstance();
        // start of next week (end of this week)
        assertTrue(now.before(calendar));
    }

    private void testCalendarIsBeginningOfWeek(Calendar calendar) {
        assertEquals(calendar.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 0);
    }

    @Test
    public void itGetsStartOfMonth() {
        Calendar calendar = DateUtils.getStartOfMonth();
        Calendar now = Calendar.getInstance();

        assertEquals(calendar.get(Calendar.MONTH), now.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 1);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 0);
    }

    @Test
    public void itGetsEndOfMonth() {
        Calendar calendar = DateUtils.getEndOfMonth();
        Calendar now = Calendar.getInstance();

        assertEquals(calendar.get(Calendar.MONTH), now.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), now.getActualMaximum(Calendar.DAY_OF_MONTH));
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(calendar.get(Calendar.MINUTE), 59);
        assertEquals(calendar.get(Calendar.SECOND), 59);
    }

    @Test
    public void itGetsStartOfYear() {
        Calendar calendar = DateUtils.getStartOfYear();
        Calendar now = Calendar.getInstance();

        assertEquals(calendar.get(Calendar.YEAR), now.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), Calendar.JANUARY);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 1);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 0);
    }

    @Test
    public void itGetsEndOfYear() {
        Calendar calendar = DateUtils.getEndOfYear();
        Calendar now = Calendar.getInstance();

        assertEquals(calendar.get(Calendar.YEAR), now.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), Calendar.DECEMBER);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 31);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(calendar.get(Calendar.MINUTE), 59);
        assertEquals(calendar.get(Calendar.SECOND), 59);
    }
}
