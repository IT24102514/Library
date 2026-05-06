package com.example.librarymanagementsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve static resources from webapp directory
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/", "file:src/main/webapp/css/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/", "file:src/main/webapp/js/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", "file:src/main/webapp/images/")
                .setCachePeriod(0);
        
        // Serve uploaded files
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String uploadLocation = "file:" + uploadPath.toString().replace("\\", "/") + "/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
        
        // Serve WEB-INF resources (for JSP support)
        registry.addResourceHandler("/WEB-INF/**")
                .addResourceLocations("/WEB-INF/");
    }
}
