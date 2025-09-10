package com.ciptadana.dashboard.database.oracle.repository.jpa.projection;

import java.math.BigDecimal;

public interface CashPositionProjection {
    String getClientid();
    BigDecimal getBuyingpower();
    BigDecimal getLoanbalance();
    BigDecimal getLoanratio();
    BigDecimal getMarketvalue();
    BigDecimal getModifiedmarketvalue();
    BigDecimal getModifiedloanratio();
    BigDecimal getTradinglimit();
    String getClienttypecode();
    String getRating();
    String getClientRating();
    BigDecimal getRiskcontrol();
    BigDecimal getOrderpower();
    String getUsername();
    String getUsertype();
    String getIsAutomaticShortsell();
    String getPriv();
    BigDecimal getOutstandingbov();
    BigDecimal getTradebuy();
    BigDecimal getTradesell();
    BigDecimal getCashValue();
    BigDecimal getAccPayable();
}