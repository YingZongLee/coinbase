package com.cathaybk.coinbase.vo;

import com.cathaybk.coinbase.exception.APIResponseCode;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ApiResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return (ApiResponse<T>) ApiResponse.builder()
            .code(APIResponseCode.SUCCESS.getStatus())
            .message(APIResponseCode.SUCCESS.getMessage())
            .data(data)
            .build();
    }
}
