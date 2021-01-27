package com.example;

import com.example.commons.BaseResponse;
import com.example.commons.CompileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(CompileException.class)
    public ResponseEntity<?> compileException(CompileException e) {
        log.info("", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    private static class ErrorResponse extends BaseResponse<Void> {
        public ErrorResponse(String message) {
            super(-1, message);
        }
    }
}
