package com.cathaybk.coinbase.controller;

import com.cathaybk.coinbase.exception.APIException;
import com.cathaybk.coinbase.exception.APIResponseCode;
import com.cathaybk.coinbase.exception.ApiProcessingException;
import com.cathaybk.coinbase.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.cathaybk.coinbase.utils.OkHttpUtils.simpleGet;

@Slf4j
@RestController
@RequestMapping("api/coindesk")
public class CoinDeskController {

    @ResponseBody
    @GetMapping("/bpi/price")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> getBpiPrice() throws APIException {
        try {
            return ResponseEntity.ok((Map<String, Object>) simpleGet(CurrencyService.BPI_URL, HashMap.class));
        } catch (ApiProcessingException e) {
            log.info("bpi get;failed;{}", e.toString());
            throw new APIException(APIResponseCode.TIMEOUT, e);
        } catch (Exception e) {
            log.error("bpi get;failed;{}", e.toString());
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }
}
