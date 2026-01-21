package com.venus.kyc.exporter.mcs.controller;

import com.venus.kyc.exporter.mcs.service.EmailService;
import com.venus.kyc.exporter.mcs.service.ExcelExportService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExportController {

    private final ExcelExportService excelExportService;
    private final EmailService emailService;

    public ExportController(ExcelExportService excelExportService, EmailService emailService) {
        this.excelExportService = excelExportService;
        this.emailService = emailService;
    }

    @GetMapping("/export")
    public ResponseEntity<?> export(@RequestParam(defaultValue = "false") boolean sendEmail) {
        try {
            byte[] content = excelExportService.exportToExcel();
            String filename = "export.xlsx";

            if (sendEmail) {
                emailService.sendFileByEmail(content, filename);
                return ResponseEntity.ok("Export successful and email sent.");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);

        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during export: " + e.getMessage());
        }
    }
}
