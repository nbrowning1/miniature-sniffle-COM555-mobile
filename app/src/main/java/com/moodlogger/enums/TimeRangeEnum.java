package com.moodlogger.enums;

public enum TimeRangeEnum {

    Week("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"),
    Month("W1", "W2", "W3", "W4", "W5"),
    Year("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec");

    private String[] timeRangeValues;

    TimeRangeEnum(String... timeRangeValues) {
        this.timeRangeValues = timeRangeValues;
    }

    public String[] getTimeRangeValues() {
        return timeRangeValues;
    }

    public static TimeRangeEnum getEnum(String value) {
        if (value.contains("Week")) {
            return Week;
        } else if (value.contains("Month")) {
            return Month;
        } else if (value.contains("Year")) {
            return Year;
        }
        throw new RuntimeException("Unrecognised time range enum for value: " + value);
    }
}
