package com.moodlogger.enums;

public enum ThemeEnum {

    Default(0),
    Dark(1),
    Ocean(2),
    Mint(3);

    private int themeId;

    ThemeEnum(int themeId) {
        this.themeId = themeId;
    }

    public static ThemeEnum getTheme(int themeId) {
        for (ThemeEnum theme : ThemeEnum.values()) {
            if (theme.themeId == themeId) {
                return theme;
            }
        }
        throw new RuntimeException("Theme not found for themeId: " + themeId);
    }
}
