package com.cathaybk.coinbase.service;

import com.cathaybk.coinbase.entity.Currency;
import com.cathaybk.coinbase.exception.ApiProcessingException;
import com.cathaybk.coinbase.exception.ResourceNotFoundException;
import com.cathaybk.coinbase.vo.BitCoin;
import com.cathaybk.coinbase.vo.CurrencyVO;
import com.cathaybk.coinbase.vo.PageVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

public interface ICurrencyService {
    BitCoin getBpiPrice() throws ApiProcessingException, JsonProcessingException;
    Currency getCurrency(Long id) throws ResourceNotFoundException;
    PageVO<Currency> getCurrency(Pageable pageable);
    Currency addCurrency(CurrencyVO currency);
    Currency updateCurrency(CurrencyVO currency, Long id) throws ResourceNotFoundException;
    void deleteCurrency(Long id) throws ResourceNotFoundException;
}
