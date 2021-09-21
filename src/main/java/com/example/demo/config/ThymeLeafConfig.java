package com.example.demo.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class ThymeLeafConfig {
	
	public static final String rscFolder = "/DEV/Workspace/MyTemplate/WebContent" ;
	public static final String htmlFolder = rscFolder + "/html" ;

    public ThymeLeafConfig() {
    }

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5")
    public AbstractConfigurableTemplateResolver templateResolver() {
    	AbstractConfigurableTemplateResolver templateResolver = new FileTemplateResolver ();
    	
    	String externalFolder = ThymeLeafConfig.htmlFolder ; 
    	//externalFolder = "" ; // Do not use external folder
    	
    	if( null == externalFolder || externalFolder.length() < 1 ) {
    		templateResolver = new ClassLoaderTemplateResolver();
    	} else if ( ! externalFolder.endsWith( "/")  ) {
    		externalFolder += "/" ; 
    	}

        templateResolver.setPrefix( externalFolder );
        //templateResolver.setSuffix(".html");
        // templateResolver.setTemplateMode("HTML5");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    @Bean
    @Description("Thymeleaf view resolver")
    public ViewResolver viewResolver() {
        var viewResolver = new ThymeleafViewResolver();

        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    } 
    
}
