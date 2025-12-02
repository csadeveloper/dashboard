package com.ciptadana.dashboard.database.oracle.repository.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashPositionDTO {
    private String clientid;
    private BigDecimal buyingpower;
    private BigDecimal loanbalance;
    private BigDecimal loanratio;
    private BigDecimal marketvalue;
    private BigDecimal modifiedmarketvalue;
    private BigDecimal modifiedloanvalue;
    private BigDecimal modifiedloanratio;
    private BigDecimal tradinglimit;
    private String clienttypecode;
    private String rating;
    private String clientRating;
    private BigDecimal riskcontrol;
    private BigDecimal orderpower;
    private String username;
    private String usertype;
    private String isAutomaticShortsell;
    private String priv;
    private BigDecimal outstandingbov;
    private BigDecimal tradebuy;
    private BigDecimal tradesell;
    private BigDecimal cashValue;
    private BigDecimal accPayable;
}
