package com.example.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse<T> {
    private static final Result OK_RESULT = new Result(0, "OK");

    private T payload;
    private Result result;

    public BaseResponse(T payload) {
        this.payload = payload;
        result = OK_RESULT;
    }

    public BaseResponse(Integer code, String message) {
        this.result = new Result(code, message);
    }

    @AllArgsConstructor
    @Getter
    public static class Result {
        private final Integer resultCode;
        private final String resultMessage;
    }
}
