package com.moodlogger;

/**
 * holds resource keys for different activity icons to aid in construction of activities
 *  views and creating custom activities
 */
public enum ActivityIconEnum {

    Work("activity_work"),
    Study("activity_study"),
    Relax("activity_relax"),
    Exercise("activity_exercise"),
    Travel("activity_travel");

    private String resourceKey;

    ActivityIconEnum(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getResourceKey() {
        return resourceKey;
    }
}
