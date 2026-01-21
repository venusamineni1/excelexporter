package com.venus.kyc.exporter.mcs.runner;

import com.venus.kyc.exporter.mcs.service.EmailService;
import com.venus.kyc.exporter.mcs.service.ExcelExportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class ExportRunner implements CommandLineRunner {

    private final ExcelExportService excelExportService;
    private final EmailService emailService;
    private final ApplicationContext context;

    public ExportRunner(ExcelExportService excelExportService, EmailService emailService, ApplicationContext context) {
        this.excelExportService = excelExportService;
        this.emailService = emailService;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(args).contains("--run-export")) {
            try {
                System.out.println("Starting SQL to Excel export from command line...");

                byte[] content = excelExportService.exportToExcel();
                String filename = "export_cli.xlsx";

                // Save locally
                Path path = Paths.get(filename);
                Files.write(path, content);
                System.out.println("Excel file saved to: " + path.toAbsolutePath());

                // Optional email if requested via argument
                if (Arrays.asList(args).contains("--send-email")) {
                    System.out.println("Sending export via email...");
                    emailService.sendFileByEmail(content, filename);
                    System.out.println("Email sent successfully.");
                }

                System.out.println("Export process completed successfully.");
            } catch (Exception e) {
                System.err.println("Error during CLI export: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("Shutting down application...");
                int exitCode = SpringApplication.exit(context, () -> 0);
                System.exit(exitCode);
            }
        }
    }
}
