package com.example.commons;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterParser {
    private static final Pattern PATTERN;

    static {
        String separator = String.join("|", PredicateRegistry.keySet());
        PATTERN = Pattern.compile("([a-zA-Z\\\\_0-9]+)(" + separator + ")(.*)", Pattern.DOTALL);
    }

    public static ParsedFilter parse(String expression) throws ParseException {
        Matcher matcher = PATTERN.matcher(expression);
        if (!matcher.matches()) {
            throw new ParseException(expression, 0);
        }
        String attributeName = matcher.group(1);
        ParsedFilter.ParsedFilterBuilder builder = ParsedFilter.builder()
                .attributeName(attributeName)
                .operator(matcher.group(2));
        if (matcher.groupCount() > 2 && matcher.group(3).trim().length() > 0) {
            builder.argument(matcher.group(3));
        }
        return builder
                .build();
    }

    @Builder
    @Getter
    public static class ParsedFilter {
        private final String attributeName;
        private final String operator;
        private final String argument;
    }
}
