package com.deng.usercenter.handler.exception;

import com.deng.usercenter.exception.TokenInvalidException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorBody> tokenInvalid(TokenInvalidException e) {
        return new ResponseEntity<>(
                ErrorBody.builder()
                        .body("Token非法，禁止访问!")
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .build()
                , HttpStatus.UNAUTHORIZED);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorBody {
    private String body;
    private Integer code;
}
