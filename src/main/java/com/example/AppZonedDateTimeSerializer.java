package com.example;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

public class AppZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    public AppZonedDateTimeSerializer() {
        super(TestRestDeptApplication.DATE_TIME_FORMATTER);
    }
}
