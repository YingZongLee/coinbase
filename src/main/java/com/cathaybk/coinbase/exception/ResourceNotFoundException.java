package com.cathaybk.coinbase.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException() {
        super(-1, "CURRENCY NOT FOUND");
    }
}
