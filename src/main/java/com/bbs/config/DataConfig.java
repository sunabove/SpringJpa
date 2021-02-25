package com.bbs.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*; 

@Configuration 
public class DataConfig {

    public DataConfig() {
    }

    @Bean public javax.sql.DataSource getDataSource() { 
        var dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mariadb://localhost:3306/MY_SPRING");
        dataSourceBuilder.username("MY_USER");
        dataSourceBuilder.password("admin");

        return dataSourceBuilder.build();
    }

}
