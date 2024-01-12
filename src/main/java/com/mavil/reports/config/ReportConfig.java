package com.mavil.reports.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class ReportConfig {

    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    public DataSource getDataSource() {

        // i was hoping this was going to pull my current datasource, as
        // defined in application.properties
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();

        // the call to the above method
        dataSourceInitializer.setDataSource(getDataSource());


        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

        return dataSourceInitializer;
    }

}
