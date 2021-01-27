package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class TestRestDeptApplication {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static void main(String[] args) {
        SpringApplication.run(TestRestDeptApplication.class, args);
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new StringZonedDateTimeConverter());
        return service;
    }
}
