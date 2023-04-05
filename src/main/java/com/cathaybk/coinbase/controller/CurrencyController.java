package com.cathaybk.coinbase.controller;

import com.cathaybk.coinbase.entity.Currency;
import com.cathaybk.coinbase.exception.APIException;
import com.cathaybk.coinbase.exception.APIResponseCode;
import com.cathaybk.coinbase.exception.ApiProcessingException;
import com.cathaybk.coinbase.exception.ResourceNotFoundException;
import com.cathaybk.coinbase.service.CurrencyService;
import com.cathaybk.coinbase.vo.ApiResponse;
import com.cathaybk.coinbase.vo.BitCoin;
import com.cathaybk.coinbase.vo.CurrencyVO;
import com.cathaybk.coinbase.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @ResponseBody
    @GetMapping("/bpi/price")
    public ResponseEntity<ApiResponse<BitCoin>> getBpiPrice() throws APIException {
        try {
            return ResponseEntity.ok(ApiResponse.success(currencyService.getBpiPrice()));
        } catch (ApiProcessingException e) {
            log.info("bpi get;failed;{}", e.toString());
            throw new APIException(APIResponseCode.CURRENCY_NOT_EXIST, e);
        } catch (Exception e) {
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<PageVO<Currency>> getCurrency(
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size", required = false, defaultValue = "100") int size) {
        return ResponseEntity.ok(currencyService.getCurrency(PageRequest.of(page, size)));
    }

    @ResponseBody
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<Currency>> getCurrency(
        @PathVariable Long id) throws APIException {
        try {
            log.info("currency get;id={}", id);
            Currency _currency = currencyService.getCurrency(id);
            return ResponseEntity.ok(ApiResponse.success(_currency));
        } catch (ResourceNotFoundException e) {
            log.error("currency get;failed;id={}", id);
            throw new APIException(APIResponseCode.CURRENCY_NOT_EXIST, e);
        } catch (Exception e) {
            log.error("currency update;failed;{}", e.toString());
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<ApiResponse<Currency>> addCurrency(
        @RequestBody CurrencyVO currency) throws APIException {
        try {
            log.info("currency add;{}", currency);
            Currency _currency = currencyService.addCurrency(currency);
            return ResponseEntity.accepted().body(ApiResponse.success(_currency));
        } catch (Exception e) {
            log.error("currency add;failed;{}", currency);
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Currency>> updateCurrency(
        @PathVariable Long id, @RequestBody CurrencyVO currency) throws APIException {
        try {
            log.info("currency update;id={}", id);
            Currency _currency = currencyService.updateCurrency(currency, id);
            return ResponseEntity.ok(ApiResponse.success(_currency));
        } catch (ResourceNotFoundException e) {
            log.error("currency update;failed;id={}", id);
            throw new APIException(APIResponseCode.CURRENCY_NOT_EXIST, e);
        } catch (Exception e) {
            log.error("currency update;failed;{}", e.toString());
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable Long id) throws APIException {
        try {
            log.info("currency delete;id={}", id);
            currencyService.deleteCurrency(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("currency delete;failed;id={}", id);
            throw new APIException(APIResponseCode.CURRENCY_NOT_EXIST, e);
        } catch (Exception e) {
            log.error("currency delete;failed;{}", e.toString());
            throw new APIException(APIResponseCode.GENERAL_ERROR, e);
        }
    }
}
