package com.moodlogger.db.entities;

import java.util.Objects;

public class Activity extends Entity {

    public static final String TABLE_NAME = "activity";
    public static final String NAME = "name";
    public static final String IMG_KEY = "img_key";

    private long id;
    private String name;
    private String imgKey;

    public Activity(String name, String imgKey) {
        this(-1L, name, imgKey);
    }

    public Activity(long id, String name, String imgKey) {
        this.id = id;
        this.name = name;
        this.imgKey = imgKey;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgKey() {
        return imgKey;
    }

    /* used specifically for making it easier to create a distinct list of activities
        on the View tab and comparing Activities on Evaluate tab */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Activity)) {
            return false;
        }

        final Activity other = (Activity) obj;

        return this.getId() == other.getId() &&
                this.getName().equals(other.getName());
    }

    /* used specifically for making it easier to create a distinct list of activities
        on the View tab and comparing Activities on Evaluate tab */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
