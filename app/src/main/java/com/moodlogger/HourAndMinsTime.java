package com.moodlogger;

public class HourAndMinsTime {

    private int hour;
    private int minute;

    public HourAndMinsTime(String time) {
        this.hour = Integer.valueOf(time.substring(0, 2));
        this.minute = Integer.valueOf(time.substring(3, 5));
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
