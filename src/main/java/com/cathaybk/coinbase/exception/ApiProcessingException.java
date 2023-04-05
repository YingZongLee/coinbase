package com.cathaybk.coinbase.exception;

import lombok.Getter;

@Getter
public class ApiProcessingException extends BaseException {

    public ApiProcessingException() {
        super(-2, "TIMEOUT");
    }
}
