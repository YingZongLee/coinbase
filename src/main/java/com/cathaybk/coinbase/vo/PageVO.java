package com.cathaybk.coinbase.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PageVO<T> implements Serializable {
    private int number;
    private int size;
    private int totalCount;
    private int totalPage;
    private List<T> result;
}
