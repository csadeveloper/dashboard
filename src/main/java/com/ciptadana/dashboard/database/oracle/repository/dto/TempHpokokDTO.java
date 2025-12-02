package com.ciptadana.dashboard.database.oracle.repository.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TempHpokokDTO {
    private String nclient;
    private String nshare;
    private BigDecimal price;
}
