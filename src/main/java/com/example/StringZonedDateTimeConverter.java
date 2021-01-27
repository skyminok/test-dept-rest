package com.example;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;

public class StringZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    @Override
    @SuppressWarnings("NullableProblems")
    public ZonedDateTime convert(String source) {
        return ZonedDateTime.parse(source, TestRestDeptApplication.DATE_TIME_FORMATTER);
    }
}
