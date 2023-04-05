package com.cathaybk.coinbase.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class BitCoin implements Serializable {
    private String name;
    private String code;
    private String updateAt;
    private List<CurrencyVO> currencies;
}
