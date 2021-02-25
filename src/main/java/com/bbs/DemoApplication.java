package com.bbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bbs.config.ThymeLeafConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RestController
@ComponentScan
//@EnableJpaRepositories( "com.bbs.model" )
//@EntityScan( basePackages = {"com.bbs.model"} )
public class DemoApplication implements WebMvcConfigurer  {
	
	public static final String LINE = "#".repeat(40);
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info(LINE);
		String funName = "addResourceHandlers(ResourceHandlerRegistry registry)";		
		log.info(funName);

		// resource (css,js,img) resource location
		if (!registry.hasMappingForPattern("/rsc/**")) {
			registry
			.addResourceHandler("/rsc/**")
			.addResourceLocations("file:" + ThymeLeafConfig.rscFolder + "/");
		}

		log.info("Done. " + funName);
		log.info(LINE);
	} 
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
	
}
