package com.cathaybk.coinbase.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum APIResponseCode {
    SUCCESS(35000, HttpStatus.OK, "success"),
    GENERAL_ERROR(35001, HttpStatus.INTERNAL_SERVER_ERROR, "general.error"),
    CURRENCY_NOT_EXIST(35002, HttpStatus.BAD_REQUEST, "currency.not.exist"),
    TIMEOUT(35003, HttpStatus.BAD_REQUEST, "timeout");

    private final int status;
    private final HttpStatus httpStatus;
    private final String message;


}
