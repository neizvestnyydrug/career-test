package ru.project.careertest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/test/schoolboy").setViewName("test");
        registry.addViewController("/test/student").setViewName("test");
        registry.addViewController("/test/working").setViewName("test");
        registry.addViewController("/result").setViewName("result");
    }
}
