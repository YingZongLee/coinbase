package com.cathaybk.coinbase.service;

import com.cathaybk.coinbase.entity.Currency;
import com.cathaybk.coinbase.exception.ApiProcessingException;
import com.cathaybk.coinbase.exception.ResourceNotFoundException;
import com.cathaybk.coinbase.repository.CurrencyRepository;
import com.cathaybk.coinbase.vo.BitCoin;
import com.cathaybk.coinbase.vo.CurrencyVO;
import com.cathaybk.coinbase.vo.PageVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cathaybk.coinbase.utils.OkHttpUtils.simpleGet;

@Slf4j
@Service
public class CurrencyService implements ICurrencyService {

    public static final String BPI_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    public static final String BPI_NAME = "BITCOIN";
    public static final String BPI_CODE = "BPI";
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public BitCoin getBpiPrice() throws ApiProcessingException {
        String updateAt, coinName;
        try {
            List<CurrencyVO> currencyVOList = new ArrayList<>();
            String jsonStr = simpleGet(BPI_URL, String.class);
            JsonNode node = new ObjectMapper().readTree(jsonStr);
            updateAt = node.hasNonNull("time")
                ? formatDate(node.get("time").get("updatedISO").textValue())
                : StringUtils.EMPTY;
            coinName = node.hasNonNull("chartName")
                ? node.get("chartName").textValue()
                : StringUtils.EMPTY;
            if (BPI_NAME.equalsIgnoreCase(coinName) && node.hasNonNull(BPI_CODE.toLowerCase())) {
                Currency bpi = currencyRepository.getByCode(BPI_CODE.toUpperCase());
                node.get(BPI_CODE.toLowerCase()).forEach(each -> {
                    String code = each.hasNonNull("code") ? each.get("code").textValue() : StringUtils.EMPTY;
                    Currency currency = StringUtils.isNotEmpty(code) ? currencyRepository.getByCode(code.toUpperCase()) : null;
                    currencyVOList.add(CurrencyVO.builder()
                        .code(code)
                        .name(
                            Objects.nonNull(currency)
                                ? String.format("%s/%s",
                                currency.getName(),
                                Objects.nonNull(bpi)
                                    ? bpi.getName()
                                    : StringUtils.EMPTY)
                                : null)
                        .rate(
                            Objects.nonNull(currency)
                                ? each.get("rate_float").floatValue()
                                : NumberUtils.FLOAT_ZERO)
                        .build());
                });
                return BitCoin.builder()
                    .code(coinName)
                    .name(bpi.getName())
                    .updateAt(updateAt)
                    .currencies(currencyVOList)
                    .build();
            }
        } catch (Exception e) {
            log.error("get bpi price;failed;{}", e.toString());
            throw new ApiProcessingException();
        }
        return BitCoin.builder().build();
    }

    @Override
    public Currency getCurrency(Long id) throws ResourceNotFoundException {
        return currencyRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PageVO<Currency> getCurrency(Pageable pageable) {
        Page<Currency> _currency = currencyRepository.findAll(pageable);
        if (_currency.isEmpty()) {
            return PageVO.<Currency>builder()
                .result(Collections.emptyList())
                .build();
        }
        return PageVO.<Currency>builder()
            .totalPage(_currency.getTotalPages())
            .totalCount(_currency.getNumberOfElements())
            .number(_currency.getNumber())
            .size(_currency.getSize())
            .result(_currency.getContent())
            .build();
    }

    @Override
    public Currency addCurrency(CurrencyVO currency) {
        return currencyRepository.save(
            Currency.builder()
                .code(currency.getCode())
                .name(currency.getName())
                .createTime(new Date())
                .updateTime(new Date())
                .build());
    }

    @Override
    public Currency updateCurrency(CurrencyVO currency, Long id) throws ResourceNotFoundException {
        Currency _currency = getCurrency(id);
        _currency.setName(currency.getName());
        _currency.setCode(currency.getCode());
        _currency.setUpdateTime(new Date());
        return currencyRepository.save(_currency);
    }

    @Override
    public void deleteCurrency(Long id) throws ResourceNotFoundException {
        getCurrency(id);
        currencyRepository.deleteById(id);
    }

    private String formatDate(String dateStr) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:MM:ss");
        return DateTime.parse(dateStr).toString(fmt);
    }
}
