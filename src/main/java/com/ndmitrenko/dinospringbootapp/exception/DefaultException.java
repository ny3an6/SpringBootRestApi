package com.ndmitrenko.dinospringbootapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultException extends RuntimeException {

    private HttpStatus code;
    private ApiResult apiResult;

    public DefaultException(HttpStatus code, String message) {
        super(message);
        this.code = code;
        this.apiResult = ApiResult.FAIL;
    }

    public DefaultException(String message) {
        super(message);
        this.apiResult = ApiResult.FAIL;
    }
}
