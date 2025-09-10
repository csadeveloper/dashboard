package com.ciptadana.dashboard.database.oracle.repository.jpa.projection;

import java.math.BigDecimal;

public interface PortfolioListProjection {

    String getNclient();

    String getNshare();

    BigDecimal getPortfolioUser();

    BigDecimal getStockOutstanding();

    BigDecimal getMatchBuy();

    BigDecimal getMatchSell();

    BigDecimal getAvgPrice();

    String getTransModule();
}
