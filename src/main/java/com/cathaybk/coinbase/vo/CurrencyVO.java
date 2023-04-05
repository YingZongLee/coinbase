package com.cathaybk.coinbase.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyVO {
    private String code;
    private String name;
    private Float rate;
}
