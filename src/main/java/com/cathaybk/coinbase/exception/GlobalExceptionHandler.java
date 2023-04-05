package com.cathaybk.coinbase.exception;

import com.cathaybk.coinbase.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(APIException.class)
    public ApiResponse<Object> processAPIException(HttpServletResponse response, APIException error) {
        APIResponseCode responseCode = error.getApiResponseCode();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
            .code(responseCode.getStatus())
            .message(responseCode.getMessage())
            .build();
        response.setStatus(responseCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        return apiResponse;
    }
}
