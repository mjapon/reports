package com.mavil.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ReportsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
		 /*String JASPER_TYPE_FILE = ".jasper";
		 String JRXML_TYPE_FILE = ".jrxml";

		String reportPath = "/opt/reportes/mavil/templateFacturaYolis.jrxml";

		String compiledReportPath = reportPath.substring(0, reportPath.lastIndexOf(JRXML_TYPE_FILE)).concat(JASPER_TYPE_FILE);

		System.out.println(compiledReportPath);
		System.out.println(reportPath);*/
        SpringApplication.run(ReportsApplication.class, args);
    }

}
