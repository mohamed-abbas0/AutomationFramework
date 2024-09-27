package org.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataSheet {
    Map<String, Integer> columnMap;
    Sheet sheet;

    public ExcelDataSheet(String excelFilePath, String sheetName) throws FileNotFoundException {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

             sheet = workbook.getSheet(sheetName);

            // Read the header row to map column names to indices
            Row headerRow = sheet.getRow(0);
            columnMap = new HashMap<>();
            for (Cell cell : headerRow) {
                columnMap.put(cell.getStringCellValue(), cell.getColumnIndex());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getColumnIndex(String columnName) {
        if (!columnMap.containsKey(columnName)) {
            throw new RuntimeException("Column not found: " + columnName);
        }
        else return columnMap.get(columnName);
    }

    public int getRowIndex(String rowName) {
        if (columnMap.containsKey("testCases")) {
            int targetRowIndex = -1;
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell firstCell = row.getCell(columnMap.get("testCases"));
                    if (firstCell != null && firstCell.getStringCellValue().equals(rowName)) {
                        targetRowIndex = rowIndex;
                        break;
                    }
                }
            }
            return targetRowIndex;
        }
        else throw new RuntimeException("Row not found: " + rowName);
    }

    public String getCellValue(int rowIndex, int columnIndex) {
        return new DataFormatter().formatCellValue(sheet.getRow(rowIndex).getCell(columnIndex));
    }

    public void setCellValue(int rowIndex, int columnIndex, String cellValue) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(columnIndex);
        cell.setCellValue(cellValue);
    }
}
