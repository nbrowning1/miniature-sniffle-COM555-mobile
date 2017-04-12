package com.moodlogger.utils;

public class StringUtils {

    public static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
}
