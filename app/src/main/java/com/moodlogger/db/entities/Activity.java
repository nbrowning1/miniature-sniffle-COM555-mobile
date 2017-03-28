package com.moodlogger.db.entities;

public class Activity extends Entity {

    public static final String TABLE_NAME = "activity";
    public static final String NAME = "name";
    public static final String IMG_KEY = "img_key";

    public String name;
    public int imgKey;

    public Activity(String name, int imgKey) {
        this.name = name;
        this.imgKey = imgKey;
    }
}
