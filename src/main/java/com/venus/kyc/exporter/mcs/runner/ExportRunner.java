package com.venus.kyc.exporter.mcs.runner;

import com.venus.kyc.exporter.mcs.service.EmailService;
import com.venus.kyc.exporter.mcs.service.ExcelExportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class ExportRunner implements CommandLineRunner {

    private final ExcelExportService excelExportService;
    private final EmailService emailService;

    public ExportRunner(ExcelExportService excelExportService, EmailService emailService) {
        this.excelExportService = excelExportService;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(args).contains("--run-export")) {
            System.out.println("Starting SQL to Excel export from command line...");

            byte[] content = excelExportService.exportToExcel();
            String filename = "export_cli.xlsx";

            // Save locally
            Path path = Paths.get(filename);
            Files.write(path, content);
            System.out.println("Excel file saved to: " + path.toAbsolutePath());

            // Optional email if requested via argument or can be default if configured
            if (Arrays.asList(args).contains("--send-email")) {
                System.out.println("Sending export via email...");
                emailService.sendFileByEmail(content, filename);
                System.out.println("Email sent successfully.");
            }

            System.out.println("Export process completed.");
        }
    }
}
