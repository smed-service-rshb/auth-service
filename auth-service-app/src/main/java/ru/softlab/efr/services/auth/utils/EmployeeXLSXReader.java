package ru.softlab.efr.services.auth.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.model.Segment;
import ru.softlab.efr.services.auth.utils.model.EmployeeParseResult;
import ru.softlab.efr.services.auth.utils.model.ParseError;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Загрузчик-парсер XLSX файла с информацией о пользователях
 *
 * @author olshansky
 * @since 21.09.2018.
 */
public class EmployeeXLSXReader {

    private static final Integer PERSONNEL_NUMBER_CELL_NUMBER = 10;
    private static final Integer LAST_NAME_CELL_NUMBER = 2;
    private static final Integer FIRST_NAME_CELL_NUMBER = 3;
    private static final Integer MIDDLE_NAME_CELL_NUMBER = 4;
    private static final Integer ORGUNIT_CELL_NUMBER = 5;

    private static final Integer EMAIL_CELL_NUMBER = 12;
    private static final Integer MOBILE_PHONE_NUMBER_CELL_NUMBER = 14;
    private static final Integer INTERNAL_PHONE_NUMBER_CELL_NUMBER = 15;
    private static final Integer POSITION_CELL_NUMBER = 16;
    private static final Integer SEGMENT_CELL_NUMBER = 17;
    private static final Integer ROLE_CELL_NUMBER = 18;
    private static final Integer BRANCH_NAME_CELL_NUMBER = 19;

    private static final Integer STATUS_UPDATE_CELL_NUMBER = 0;

    public static EmployeeParseResult getEmployeeListFromXLSX(String path, String sheetName, Function<String, OrgUnit> findOrgUnitFunction
            , Function<String, Segment> findSegmentFunction
            , Function<String, Role> findRoleFunction) throws Exception {

        EmployeeParseResult result = new EmployeeParseResult();

        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheet(sheetName);

            for (Row row : myExcelSheet) {
                if (row.getRowNum() > 0) {
                    Employee employee = new Employee();
                    if (row.getCell(PERSONNEL_NUMBER_CELL_NUMBER) != null) {
                        try {
                            employee.setPersonnelNumber(getString(row.getCell(PERSONNEL_NUMBER_CELL_NUMBER)));
                            if (!employee.getPersonnelNumber().isEmpty()) {
                                employee.setLogin(employee.getPersonnelNumber());
                                employee.setSecondName(getString(row.getCell(LAST_NAME_CELL_NUMBER)));
                                employee.setFirstName(getString(row.getCell(FIRST_NAME_CELL_NUMBER)));
                                employee.setMiddleName(getString(row.getCell(MIDDLE_NAME_CELL_NUMBER)));
                                employee.setEmail(getString(row.getCell(EMAIL_CELL_NUMBER)));
                                employee.setMobilePhone(getString(row.getCell(MOBILE_PHONE_NUMBER_CELL_NUMBER)));
                                employee.setPosition(getString(row.getCell(POSITION_CELL_NUMBER)));

                                Set<OrgUnit> orgUnits = new HashSet<>();
                                orgUnits.add(findOrgUnitFunction.apply(getString(row.getCell(ORGUNIT_CELL_NUMBER))));
                                employee.setOrgUnits(orgUnits);
                                employee.setSegment(findSegmentFunction.apply(getString(row.getCell(SEGMENT_CELL_NUMBER))));
                                employee.setRoles(Collections.singletonList(findRoleFunction.apply(getString(row.getCell(ROLE_CELL_NUMBER)))));
                                employee.setDeleted(false);
                                employee.setId((long) row.getRowNum());
                                result.getParsedEmployees().add(employee);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            result.getErrors().add(new ParseError((long) row.getRowNum(), ex.getMessage()));
                        }
                    }
                }
            }
            myExcelBook.close();
        }
        List<Employee> duplicates = getDuplicates(result.getParsedEmployees());
        duplicates.forEach(f -> result.getErrors().add(new ParseError(f.getId(),
                        String.format("Найден дублирующий табельный номер %s", f.getPersonnelNumber()))));
        return result;
    }

    private static List<Employee> getDuplicates(List<Employee> employees) {
        Set<String> uniques = new HashSet<>();
        return employees.stream().filter(e -> !uniques.add(e.getPersonnelNumber())).collect(Collectors.toList());
    }

    private static String getString(Cell cell) {
        if (cell != null) {
            String value = String.valueOf(cell).trim();
            return value.replace(".0", "");
        }
        return "";
    }

    public static void setSyncErrors2XLSX(String path, String sheetName, List<ParseError> errors) throws Exception {
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheet(sheetName);
            errors.forEach(err -> {
                for (Row row : myExcelSheet) {
                    if (row.getRowNum() > 0) {
                        String personnelNumber = getString(row.getCell(PERSONNEL_NUMBER_CELL_NUMBER));
                        if (personnelNumber != null && !personnelNumber.isEmpty()) {
                            if (Long.valueOf(row.getRowNum()).equals(err.getKey())) {
                                setErrorText(row, err.getValue());
                            }
                        }
                        personnelNumber = null;
                    }
                }
            });
            myExcelBook.write(new FileOutputStream(path));
            myExcelBook.close();
        }
    }

    private static void setErrorText(Row row, String errorText) {
        Cell cellResponse = row.getCell(STATUS_UPDATE_CELL_NUMBER);
        String prevCellValue = getString(cellResponse);
        if (cellResponse != null) {
            cellResponse.setCellValue(
                    prevCellValue == null || prevCellValue.isEmpty() ?
                            errorText : prevCellValue.concat(";").concat(errorText));
        }
    }
}
