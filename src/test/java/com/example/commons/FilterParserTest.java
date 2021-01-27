package com.example.commons;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class FilterParserTest {

    @Test
    void basicTest() throws ParseException {
        FilterParser.ParsedFilter result = FilterParser.parse("asdhdcABCDE_1>=1234");
        assertNotNull(result);
        assertEquals("asdhdcABCDE_1", result.getAttributeName());
        assertEquals(">=", result.getOperator());
        assertEquals("1234", result.getArgument());
    }

    @Test
    void testNullArgument() throws ParseException {
        FilterParser.ParsedFilter result = FilterParser.parse("asdhdcABCDE_1=");
        assertNotNull(result);
        assertEquals("asdhdcABCDE_1", result.getAttributeName());
        assertEquals("=", result.getOperator());
        assertNull(result.getArgument());
    }

    @Test
    void testParseError() {
        assertThrows(ParseException.class, () -> FilterParser.parse("asdhdcABCDE_1+1"));
        assertThrows(ParseException.class, () -> FilterParser.parse("asdhdcABCDE_1+1"));
    }
}