package com.ciptadana.dashboard.database.oracle.repository.jpa.projection;

import java.math.BigDecimal;

public interface TempHpokokProjection {
    String getNclient();
    String getNshare();
    BigDecimal getPrice();
}