package com.venus.kyc.exporter.mcs.service;

import com.venus.kyc.exporter.mcs.config.ExportProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {

    private final JdbcTemplate jdbcTemplate;
    private final ExportProperties exportProperties;

    public ExcelExportService(JdbcTemplate jdbcTemplate, ExportProperties exportProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.exportProperties = exportProperties;
    }

    public byte[] exportToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (ExportProperties.QueryConfig config : exportProperties.getQueries()) {
                Sheet sheet = workbook.createSheet(config.getSheetName());
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(config.getSql());

                if (rows.isEmpty()) {
                    continue;
                }

                // Header
                Row headerRow = sheet.createRow(0);
                String[] columns = rows.get(0).keySet().toArray(new String[0]);
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }

                // Data
                int rowNum = 1;
                for (Map<String, Object> row : rows) {
                    Row excelRow = sheet.createRow(rowNum++);
                    for (int i = 0; i < columns.length; i++) {
                        Object value = row.get(columns[i]);
                        Cell cell = excelRow.createCell(i);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    }
                }

                // Auto-size columns
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
