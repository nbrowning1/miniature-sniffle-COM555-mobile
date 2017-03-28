package com.moodlogger.db.entities;

public class Mood extends Entity {

    public static final String TABLE_NAME = "mood";
    public static final String NAME = "name";
    public static final String RATING = "rating";

    public String name;
    public int rating;

    public Mood(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }
}
