package com.moodlogger.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void itReturnsEmptyForNull() {
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    public void itReturnsEmptyForEmptyString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    public void itReturnsNotEmptyForNotEmptyString() {
        assertFalse(StringUtils.isEmpty("Test"));
    }
}
