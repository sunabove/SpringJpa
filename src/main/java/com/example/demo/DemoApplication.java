package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.config.ThymeLeafConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan
//@EnableJpaRepositories( "com.bbs" )
//@EntityScan( basePackages = {"com.bbs"} )
public class DemoApplication implements WebMvcConfigurer {
	
	public static final String LINE = "#".repeat(80);
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		var regiTrue = 1 == 2 ;
		if( regiTrue ) { 
			registry.addViewController("/").setViewName("index.html"); 
		}
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info(LINE);
		String funName = "ADD RESOURCE HANDLER";		
		log.info(funName);

		// resource (css,js,img) resource location
		if (!registry.hasMappingForPattern("**/rsc/**")) {
			registry
			.addResourceHandler("**/rsc/**")
			.addResourceLocations("file:" + ThymeLeafConfig.rscFolder + "/");
		}

		log.info("Done. " + funName);
		log.info(LINE);
	} 
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
