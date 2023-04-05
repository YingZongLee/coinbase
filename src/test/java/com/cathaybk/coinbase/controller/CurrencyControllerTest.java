package com.cathaybk.coinbase.controller;

import com.cathaybk.coinbase.config.OkHttpConfig;
import com.cathaybk.coinbase.entity.Currency;
import com.cathaybk.coinbase.exception.APIException;
import com.cathaybk.coinbase.exception.APIResponseCode;
import com.cathaybk.coinbase.exception.ResourceNotFoundException;
import com.cathaybk.coinbase.repository.CurrencyRepository;
import com.cathaybk.coinbase.service.CurrencyService;
import com.cathaybk.coinbase.vo.ApiResponse;
import com.cathaybk.coinbase.vo.BitCoin;
import com.cathaybk.coinbase.vo.CurrencyVO;
import com.cathaybk.coinbase.vo.PageVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CurrencyController.class, CurrencyService.class})
@ContextConfiguration(classes = OkHttpConfig.class)
@ActiveProfiles({"test"})
public class CurrencyControllerTest {

    @Autowired
    CurrencyController currencyController;
    @Autowired
    CurrencyService currencyService;
    @MockBean
    CurrencyRepository currencyRepository;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void getBpiPrice() throws APIException {
        Mockito.when(currencyRepository.getByCode("BPI"))
            .thenReturn(
                Currency.builder().code("BPI").name("比特幣").build());
        Mockito.when(currencyRepository.getByCode("EUR"))
            .thenReturn(
                Currency.builder().code("EUR").name("歐元").build());
        Mockito.when(currencyRepository.getByCode("USD"))
            .thenReturn(
                Currency.builder().code("USD").name("美元").build());
        Mockito.when(currencyRepository.getByCode("GBP"))
            .thenReturn(
                Currency.builder().code("GBP").name("英鎊").build());
        ResponseEntity<ApiResponse<BitCoin>> data = currencyController.getBpiPrice();
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(data.getBody()).getCode(), APIResponseCode.SUCCESS.getStatus());
        System.out.println(gson.toJson(data.getBody()));
    }

    @Test
    public void getCurrency() {
        Pageable page = PageRequest.of(0, 100);
        Page<Currency> currencyPage = new PageImpl<>(
            Collections.singletonList(
                Currency.builder()
                    .id(NumberUtils.LONG_ONE)
                    .code("BPI")
                    .name("比特幣")
                    .build()),
            page,
            NumberUtils.INTEGER_ONE
        );
        Mockito.when(currencyRepository.findAll(page)).thenReturn(currencyPage);
        ResponseEntity<PageVO<Currency>> data = currencyController.getCurrency(page.getPageNumber(), page.getPageSize());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(data.getBody()).getResult(), currencyPage.getContent());
        System.out.println(gson.toJson(data.getBody()));
    }

    @Test
    public void testGetCurrency() throws APIException {
        Currency currency = Currency.builder().id(2L).code("USD").name("美元").build();
        Mockito.when(currencyRepository.findById(anyLong())).thenReturn(Optional.of(currency));
        ResponseEntity<ApiResponse<Currency>> data = currencyController.getCurrency(anyLong());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(data.getBody()).getData(), currency);
        System.out.println(gson.toJson(data.getBody()));
    }

    @Test
    public void addCurrency() throws APIException {
        Currency currency = Currency.builder()
            .code("TWD")
            .name("台幣")
            .createTime(new Date())
            .updateTime(new Date())
            .build();
        CurrencyVO vo = CurrencyVO.builder().code("TWD").name("台幣").build();
        given(currencyRepository.save(any(Currency.class))).willReturn(currency);
        ResponseEntity<ApiResponse<Currency>> data = currencyController.addCurrency(vo);
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.ACCEPTED);
        System.out.println(gson.toJson(data.getBody()));

    }

    @Test
    public void updateCurrency() throws APIException, ResourceNotFoundException {
        Currency currency = Currency.builder()
            .id(3L)
            .code("GBP")
            .name("英镑")
            .createTime(new Date())
            .updateTime(new Date())
            .build();
        CurrencyVO vo = CurrencyVO.builder().code("GBP").name("英鎊").build();
        given(currencyRepository.findById(anyLong())).willReturn(Optional.of(Currency.builder().build()));
        given(currencyRepository.save(any(Currency.class))).willReturn(currency);
        ResponseEntity<ApiResponse<Currency>> data = currencyController.updateCurrency(3L, vo);
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(data.getBody()).getData(), currency);
        System.out.println(gson.toJson(data.getBody()));
    }

    @Test
    public void deleteCurrency() throws APIException {
        given(currencyRepository.findById(anyLong())).willReturn(Optional.of(Currency.builder().build()));
        ResponseEntity<ApiResponse<Void>> data = currencyController.deleteCurrency(5L);
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}