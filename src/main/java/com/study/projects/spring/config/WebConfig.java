package com.study.projects.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {
	
	 	@Autowired
	    ApplicationContext applicationContext;
	    
	    //1. Creating SpringResourceTemplateResolver
	    @Bean
	    public SpringResourceTemplateResolver springTemplateResolver(){
	        SpringResourceTemplateResolver springTemplateResolver = new SpringResourceTemplateResolver();
	        springTemplateResolver.setApplicationContext(this.applicationContext);
	        springTemplateResolver.setPrefix("/WEB-INF/");
	        springTemplateResolver.setSuffix(".html");
	        return springTemplateResolver;
	    }
	    
	    //2. Creating SpringTemplateEngine
	    @Bean
	    public SpringTemplateEngine springTemplateEngine(){
	        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
	        springTemplateEngine.setTemplateResolver(springTemplateResolver());
	        return springTemplateEngine;
	    }
	    
	    
	    //3. Registering ThymeleafViewResolver
	    @Bean
	    public ViewResolver viewResolver(){
	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	        viewResolver.setTemplateEngine(springTemplateEngine());
	        return viewResolver;
	    }

}
