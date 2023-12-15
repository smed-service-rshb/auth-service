package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.model.MotivationReportModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MotivationXlsxReporterService {

    private static final Logger LOGGER = Logger.getLogger(MotivationXlsxReporterService.class);
    private static final short FONT_HEIGHT_TITLE = 12;
    private static final short FONT_HEIGHT_STANDARD = 10;
    private static final int COLUMN_WIDTH_STANDARD = 25;
    private CellStyle borderedHeader;
    private CellStyle borderedCell;
    private CellStyle borderedDateCell;
    private CellStyle titleStyle;
    private CellStyle sumStyle;

    private void initStyle(Workbook workbook){
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints(FONT_HEIGHT_TITLE);
        titleFont.setBold(true);

        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints(FONT_HEIGHT_STANDARD);
        headerFont.setBold(true);

        Font standardFont = workbook.createFont();
        standardFont.setFontHeightInPoints(FONT_HEIGHT_STANDARD);
        standardFont.setBold(true);

        Font fontNotBold = workbook.createFont();
        fontNotBold.setFontHeightInPoints(FONT_HEIGHT_STANDARD);

        borderedHeader = createCellStyle(true, true, headerFont, workbook);
        borderedCell = createCellStyle(true, false, standardFont, workbook);

        borderedDateCell = createCellStyle(true, false, standardFont, workbook);
        borderedDateCell.setDataFormat(workbook.createDataFormat().getFormat("MMM.yy"));
        titleStyle = createCellStyle(false, true, titleFont, workbook);
        sumStyle = createCellStyle(true, false, standardFont, workbook);
        sumStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
    }

    /**
     * Создает стиль ячейки.
     */
    private CellStyle createCellStyle(boolean bordered, boolean header, Font font, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (bordered) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.NONE);
            cellStyle.setBorderTop(BorderStyle.NONE);
            cellStyle.setBorderRight(BorderStyle.NONE);
        }
        if (header) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public String getAdminReportHeader(LocalDate startDate, LocalDate endDate) {
        return String.format("Отчет по данным на мотивацию с %s по %s на дату и время %s",
                startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    public byte[] getReport(List<MotivationReportModel> motivationReportList, LocalDate startDate, LocalDate endDate) {
        try (Workbook book = new XSSFWorkbook()) {
            initStyle(book);
            Sheet sheet = book.createSheet("Лист 1");
            sheet.setDefaultColumnWidth(COLUMN_WIDTH_STANDARD);
            Row headerRow = sheet.createRow(0);
            Cell headerRowCell = headerRow.createCell(0);
            headerRowCell.setCellValue(getAdminReportHeader(startDate, endDate));
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,16));
            headerRowCell.setCellStyle(titleStyle);
            Row firstRow = sheet.createRow(1);
            Cell name = firstRow.createCell(0);
            name.setCellValue("ФИО сотрудника");
            name.setCellStyle(borderedHeader);
            Cell employerPersonnelNumber = firstRow.createCell(1);
            employerPersonnelNumber.setCellValue("Табельный номер сотрудника");
            employerPersonnelNumber.setCellStyle(borderedHeader);
            Cell position = firstRow.createCell(2);
            position.setCellValue("Должность сотрудника");
            position.setCellStyle(borderedHeader);
            Cell office = firstRow.createCell(3);
            office.setCellValue("ВСП сотрудника");
            office.setCellStyle(borderedHeader);
            Cell orgUnit = firstRow.createCell(4);
            orgUnit.setCellValue("РФ сотрудника");
            orgUnit.setCellStyle(borderedHeader);
            Cell employerRole = firstRow.createCell(5);
            employerRole.setCellValue("Роль сотрудника");
            employerRole.setCellStyle(borderedHeader);
            Cell employerSegment = firstRow.createCell(6);
            employerSegment.setCellValue("Сегмент сотрудника");
            employerSegment.setCellStyle(borderedHeader);
            Cell accountNumber = firstRow.createCell(7);
            accountNumber.setCellValue("Номер счета в РСХБ");
            accountNumber.setCellStyle(borderedHeader);
            Cell bankBIK = firstRow.createCell(8);
            bankBIK.setCellValue("БИК Банка");
            bankBIK.setCellStyle(borderedHeader);
            Cell bankName = firstRow.createCell(9);
            bankName.setCellValue("Наименование филиала Банка");
            bankName.setCellStyle(borderedHeader);
            Cell inn = firstRow.createCell(10);
            inn.setCellValue("ИНН");
            inn.setCellStyle(borderedHeader);
            Cell registrationAddress = firstRow.createCell(11);
            registrationAddress.setCellValue("Адрес регистрации");
            registrationAddress.setCellStyle(borderedHeader);
            Cell registrationIndex = firstRow.createCell(12);
            registrationIndex.setCellValue("Индекс");
            registrationIndex.setCellStyle(borderedHeader);
            Cell latestAgreement = firstRow.createCell(13);
            latestAgreement.setCellValue("Дата последнего прикрепления согласия");
            latestAgreement.setCellStyle(borderedHeader);
            Cell latestUpdate = firstRow.createCell(14);
            latestUpdate.setCellValue("Дата последнего прикрепления паспорта");
            latestUpdate.setCellStyle(borderedHeader);
            Cell correctness = firstRow.createCell(15);
            correctness.setCellValue("Статус корректности предоставленных данных");
            correctness.setCellStyle(borderedHeader);
            Cell comment = firstRow.createCell(16);
            comment.setCellValue("Комментарий");
            comment.setCellStyle(borderedHeader);
            Cell motivationStartDate = firstRow.createCell(17);
            motivationStartDate.setCellValue("Дата начала действия согласия на мотивацию");
            motivationStartDate.setCellStyle(borderedHeader);

            int rawNumber = 2;
            for (MotivationReportModel motivationReportModel : motivationReportList) {
                Row raw = sheet.createRow(rawNumber);
                Cell nameRaw = raw.createCell(0);
                nameRaw.setCellValue(motivationReportModel.getEmployerSNM());
                nameRaw.setCellStyle(borderedCell);
                Cell employerPersonnelNumberRaw = raw.createCell(1);
                employerPersonnelNumberRaw.setCellValue(motivationReportModel.getEmployerPersonnelNumber());
                employerPersonnelNumberRaw.setCellStyle(borderedCell);
                Cell positionRaw = raw.createCell(2);
                positionRaw.setCellValue(motivationReportModel.getPosition());
                positionRaw.setCellStyle(borderedCell);
                Cell officeRaw = raw.createCell(3);
                officeRaw.setCellValue(motivationReportModel.getOffice());
                officeRaw.setCellStyle(borderedCell);
                Cell orgUnitRaw = raw.createCell(4);
                orgUnitRaw.setCellValue(motivationReportModel.getOrgUnit());
                orgUnitRaw.setCellStyle(borderedCell);
                Cell employerRoleRaw = raw.createCell(5);
                employerRoleRaw.setCellValue(motivationReportModel.getEmployerRole());
                employerRoleRaw.setCellStyle(borderedCell);
                Cell employerSegmentRaw = raw.createCell(6);
                employerSegmentRaw.setCellValue(motivationReportModel.getEmployerSegment());
                employerSegmentRaw.setCellStyle(borderedCell);
                Cell accountNumberRaw = raw.createCell(7);
                accountNumberRaw.setCellValue(motivationReportModel.getAccountNumber());
                accountNumberRaw.setCellStyle(borderedCell);
                Cell bankBIKRaw = raw.createCell(8);
                bankBIKRaw.setCellValue(motivationReportModel.getBankBIK());
                bankBIKRaw.setCellStyle(borderedCell);
                Cell bankNameRaw = raw.createCell(9);
                bankNameRaw.setCellValue(motivationReportModel.getBankName());
                bankNameRaw.setCellStyle(borderedCell);
                Cell innRaw = raw.createCell(10);
                innRaw.setCellValue(motivationReportModel.getInn());
                innRaw.setCellStyle(borderedCell);
                Cell registrationAddressRaw = raw.createCell(11);
                registrationAddressRaw.setCellValue(motivationReportModel.getRegistrationAddress());
                registrationAddressRaw.setCellStyle(borderedCell);
                Cell registrationIndexRaw = raw.createCell(12);
                registrationIndexRaw.setCellValue(motivationReportModel.getRegistrationIndex());
                registrationIndexRaw.setCellStyle(borderedCell);
                Cell latestAgreementUpdateRaw = raw.createCell(13);
                latestAgreementUpdateRaw.setCellValue(motivationReportModel.getLatestAgreementUpdate());
                latestAgreementUpdateRaw.setCellStyle(borderedDateCell);
                Cell latestPassportUpdateRaw = raw.createCell(14);
                latestPassportUpdateRaw.setCellValue(motivationReportModel.getLatestPassportUpdate());
                latestPassportUpdateRaw.setCellStyle(borderedDateCell);
                Cell correctnessRaw = raw.createCell(15);
                correctnessRaw.setCellValue(motivationReportModel.getCorrectness());
                correctnessRaw.setCellStyle(borderedCell);
                Cell commentRaw = raw.createCell(16);
                commentRaw.setCellValue(motivationReportModel.getComment());
                commentRaw.setCellStyle(borderedCell);
                Cell motivationStartDateRaw = raw.createCell(17);
                motivationStartDateRaw.setCellValue(motivationReportModel.getMotivationStartDate());
                motivationStartDateRaw.setCellStyle(borderedDateCell);
                rawNumber++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            book.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Произошла критическая ошибка при формировании отчета мотиваций: %s", e);
        }
        LOGGER.warn("Нет данных для формирования отчета мотиваций");
        return new byte[0];
    }
}
