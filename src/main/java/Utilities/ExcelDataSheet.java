package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataSheet {

    private final Map<String, Integer> columnMap;
    private final Sheet sheet;
    private final Workbook workbook;
    private final String filePath;
    private final DataFormatter formatter;

    public ExcelDataSheet(String excelFilePath, String sheetName) throws IOException {
        this.filePath = excelFilePath;
        FileInputStream fis = new FileInputStream(excelFilePath);
        workbook = new XSSFWorkbook(fis);
        fis.close();

        sheet = workbook.getSheet(sheetName);
        if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

        columnMap = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) throw new RuntimeException("Header row is missing in sheet: " + sheetName);

        for (Cell cell : headerRow) {
            columnMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        formatter = new DataFormatter();
    }

    public int getColumnIndex(String columnName) {
        if (!columnMap.containsKey(columnName)) {
            throw new RuntimeException("Column not found: " + columnName);
        }
        return columnMap.get(columnName);
    }

    public int getRowIndex(String rowName) {
        if (!columnMap.containsKey("testCases")) throw new RuntimeException("'testCases' column not found!");

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell = row.getCell(columnMap.get("testCases"));
                if (cell != null && formatter.formatCellValue(cell).equals(rowName)) {
                    return rowIndex;
                }
            }
        }
        throw new RuntimeException("Row not found: " + rowName);
    }

    public String getCellValue(int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) return "";
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return "";
        return formatter.formatCellValue(cell);
    }

    public void setCellValue(int rowIndex, int columnIndex, String value) throws IOException {
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        if (cell == null) cell = row.createCell(columnIndex);

        cell.setCellValue(value);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    public void closeWorkbook() throws IOException {
        workbook.close();
    }
}
