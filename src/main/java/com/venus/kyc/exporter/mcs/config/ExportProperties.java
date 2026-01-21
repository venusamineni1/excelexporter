package com.venus.kyc.exporter.mcs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "export")
public class ExportProperties {

    private List<QueryConfig> queries = new ArrayList<>();
    private EmailProperties email = new EmailProperties();

    public List<QueryConfig> getQueries() {
        return queries;
    }

    public void setQueries(List<QueryConfig> queries) {
        this.queries = queries;
    }

    public EmailProperties getEmail() {
        return email;
    }

    public void setEmail(EmailProperties email) {
        this.email = email;
    }

    public static class QueryConfig {
        private String sheetName;
        private String sql;

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    public static class EmailProperties {
        private String to;
        private String subject = "SQL Export Results";
        private String body = "Please find the attached SQL export results.";

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
