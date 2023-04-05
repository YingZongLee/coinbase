package com.cathaybk.coinbase.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.ServletException;

@Data
@EqualsAndHashCode(callSuper = true)
public class APIException extends ServletException {
    private APIResponseCode apiResponseCode;

    public APIException(APIResponseCode apiResponseCode, String message) {
        super(message);
        this.apiResponseCode = apiResponseCode;
    }

    public APIException(APIResponseCode apiResponseCode, Throwable throwable) {
        super(throwable);
        this.apiResponseCode = apiResponseCode;
    }
}
