package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.domain.entities.Customer;
import com.mlorenzo.besttravel.repositories.CustomerRepository;
import com.mlorenzo.besttravel.services.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private static final String SHEET_NAME = "Customer total sales";
    private static final String FONT_NAME = "Arial";
    private static final String HEADER_CUSTOMER_ID = "Id";
    private static final String HEADER_CUSTOMER_NAME = "Name";
    private static final String HEADER_CUSTOMER_PURCHASES = "Purchases";

    private final CustomerRepository customerRepository;

    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        try(final XSSFWorkbook workbook = new XSSFWorkbook()) {
            final XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            sheet.setColumnWidth(0, 7000);
            sheet.setColumnWidth(1, 7000);
            sheet.setColumnWidth(2, 7000);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            final XSSFFont headerFont = workbook.createFont();
            headerFont.setFontName(FONT_NAME);
            headerFont.setFontHeightInPoints((short) 16);
            headerFont.setBold(true);
            style.setFont(headerFont);
            final XSSFRow headerRow = sheet.createRow(0);
            XSSFCell headerCell = headerRow.createCell(0);
            headerCell.setCellValue(HEADER_CUSTOMER_ID);
            headerCell.setCellStyle(style);
            headerCell = headerRow.createCell(1);
            headerCell.setCellValue(HEADER_CUSTOMER_NAME);
            headerCell.setCellStyle(style);
            headerCell = headerRow.createCell(2);
            headerCell.setCellValue(HEADER_CUSTOMER_PURCHASES);
            headerCell.setCellStyle(style);
            style = workbook.createCellStyle();
            style.setWrapText(true);
            List<Customer> customers = (List<Customer>) customerRepository.findAll();
            for (int i = 0; i < customers.size(); i++) {
                final Customer customer = customers.get(i);
                final int totalPurchases = customer.getTotalFlights() + customer.getTotalLodgings() +
                        customer.getTotalTours();
                final XSSFRow row = sheet.createRow(i + 1);
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(customer.getDni());
                cell.setCellStyle(style);
                cell = row.createCell(1);
                cell.setCellValue(customer.getFullName());
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue(totalPurchases);
                cell.setCellStyle(style);
            }
            workbook.write(response.getOutputStream());
        }
    }
}
