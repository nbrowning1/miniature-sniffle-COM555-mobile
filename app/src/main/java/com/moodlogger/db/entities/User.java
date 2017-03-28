package com.moodlogger.db.entities;

public class User extends Entity {

    public static final String TABLE_NAME = "user";
    public static final String NAME = "name";

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
