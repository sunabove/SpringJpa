package com.example.demo.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

@Configuration
public class DataBaseConfig {

	public DataBaseConfig() {
	}

	@Bean
	public javax.sql.DataSource getDataSource() {
		var dataSourceBuilder = DataSourceBuilder.create();

		dataSourceBuilder.driverClassName("oracle.jdbc.OracleDriver");
		dataSourceBuilder.url("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSourceBuilder.username("john");
		dataSourceBuilder.password("a");

		return dataSourceBuilder.build();
	}

}
