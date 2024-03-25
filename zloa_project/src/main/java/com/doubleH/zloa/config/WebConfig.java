package com.doubleH.zloa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO Auto-generated method stub
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedMethods("GET", "POST", "PUT", "DELETE");

        // .allowedOrigins("http://localhost:8080/", "http://localhost:8081")

        WebMvcConfigurer.super.addCorsMappings(registry);
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
}