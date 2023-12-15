package ru.softlab.efr.services.test.auth.controllers.helpers;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TestMotivationReportsHelper {

    public static int readExcelSheetRowsCount(ByteArrayInputStream input) throws IOException {
        try (XSSFWorkbook excelBook = new XSSFWorkbook(input)) {
            return excelBook.getSheet(excelBook.getSheetName(0)).getPhysicalNumberOfRows();
        }
    }

}
