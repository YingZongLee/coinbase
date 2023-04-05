package com.cathaybk.coinbase.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {
    private final int code;
    private final String message;

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
