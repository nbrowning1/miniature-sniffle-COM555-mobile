package com.moodlogger;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HourAndMinsTimeTest {

    @Test
    public void itParsesTimeCorrectly() {
        HourAndMinsTime hourAndMinsTime = new HourAndMinsTime("13:05");
        assertEquals(hourAndMinsTime.getHour(), 13);
        assertEquals(hourAndMinsTime.getMinute(), 5);
    }
}
