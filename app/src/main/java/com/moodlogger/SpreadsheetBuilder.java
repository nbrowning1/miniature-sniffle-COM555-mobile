package com.moodlogger;

import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.List;

public class SpreadsheetBuilder {

    private static final int MOOD_TYPE_COLUMN = 0;
    private static final int MOOD_DATE_COLUMN = 1;
    private static final int MOOD_ACTIVITIES_COLUMN = 2;
    private static final int COLUMN_WIDTH = 4000;

    public static HSSFWorkbook buildMoodEntriesSpreadsheet(List<MoodEntry> moodEntries) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Moods");
        sheet.setColumnWidth(MOOD_TYPE_COLUMN, COLUMN_WIDTH);
        sheet.setColumnWidth(MOOD_DATE_COLUMN, COLUMN_WIDTH);
        sheet.setColumnWidth(MOOD_ACTIVITIES_COLUMN, COLUMN_WIDTH);

        HSSFFont titleFont = createFont(workbook, 12, true);
        HSSFFont defaultFont = createFont(workbook, 10, false);

        HSSFCellStyle titleCellStyle = createCellStyle(workbook, titleFont);
        HSSFCellStyle defaultCellStyle = createCellStyle(workbook, defaultFont);
        defaultCellStyle.setWrapText(true);

        createTitleRow(sheet, titleCellStyle);

        for (int i = 1; i <= moodEntries.size(); i++) {
            MoodEntry moodEntry = moodEntries.get(i - 1);
            createMoodEntryRow(sheet, i, defaultCellStyle, moodEntry);
        }

        return workbook;
    }

    private static HSSFFont createFont(HSSFWorkbook workbook, int fontHeight, boolean bold) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(bold);
        font.setItalic(false);
        return font;
    }

    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, HSSFFont font) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static void createTitleRow(HSSFSheet sheet, HSSFCellStyle cellStyle) {
        HSSFRow titleRow = sheet.createRow(0);
        createCell(titleRow, MOOD_TYPE_COLUMN, "Mood Type", cellStyle);
        createCell(titleRow, MOOD_DATE_COLUMN, "Date", cellStyle);
        createCell(titleRow, MOOD_ACTIVITIES_COLUMN, "Activities", cellStyle);
    }

    private static void createMoodEntryRow(HSSFSheet sheet, int rowIdx,
                                           HSSFCellStyle cellStyle, MoodEntry moodEntry) {
        HSSFRow row = sheet.createRow(rowIdx);
        createCell(row, MOOD_TYPE_COLUMN, MoodEnum.getLabelName(moodEntry.getMoodId()), cellStyle);
        createCell(row, MOOD_DATE_COLUMN, moodEntry.getDateTime(), cellStyle);

        StringBuilder activitiesCellTextBuilder = new StringBuilder();
        List<Activity> activities = moodEntry.getActivities();
        for (int i = 0; i < activities.size(); i++) {
            activitiesCellTextBuilder.append(activities.get(i).getName());
            if ((i + 1) < activities.size()) {
                activitiesCellTextBuilder.append(",");
            }
            activitiesCellTextBuilder.append("\n");
        }
        createCell(row, MOOD_ACTIVITIES_COLUMN, activitiesCellTextBuilder.toString(), cellStyle);
    }

    private static void createCell(HSSFRow row, int colIdx, String text, HSSFCellStyle cellStyle) {
        HSSFCell cell = row.createCell(colIdx);
        cell.setCellValue(new HSSFRichTextString(text));
        cell.setCellStyle(cellStyle);
    }
}
