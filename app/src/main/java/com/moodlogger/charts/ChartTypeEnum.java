package com.moodlogger.charts;

public enum ChartTypeEnum {

    Line,
    Bar,
    Pie;

    public static ChartTypeEnum getChartType(String value) {
        for (ChartTypeEnum chartType : ChartTypeEnum.values()) {
            if (chartType.toString().equals(value)) {
                return chartType;
            }
        }
        throw new RuntimeException("Unrecognised chart type enum for value: " + value);
    }
}
