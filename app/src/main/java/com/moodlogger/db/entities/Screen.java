package com.moodlogger.db.entities;

public class Screen extends Entity {

    public static final String TABLE_NAME = "screen";
    public static final String NAME = "name";
    public static final String GUIDE_GIVEN = "guide_given";

    public String name;
    public boolean guideGiven;

    public Screen(String name, boolean guideGiven) {
        this.name = name;
        this.guideGiven = guideGiven;
    }
}
