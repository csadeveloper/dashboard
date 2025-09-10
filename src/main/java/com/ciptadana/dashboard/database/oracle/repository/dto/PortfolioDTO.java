package com.ciptadana.dashboard.database.oracle.repository.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortfolioDTO {
    private String nclient;
    private String nshare; // Kode Saham
    private BigDecimal portfolioUser; // Jumlah Saham
    private BigDecimal stockOutstanding;
    private BigDecimal matchBuy;
    private BigDecimal matchSell;
    private BigDecimal avgPrice;
    private String transModule;
}
