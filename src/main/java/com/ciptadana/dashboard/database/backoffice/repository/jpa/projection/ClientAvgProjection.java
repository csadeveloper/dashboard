package com.ciptadana.dashboard.database.backoffice.repository.jpa.projection;

import java.math.BigDecimal;

public interface ClientAvgProjection {
    String getNclient();
    String getNshare();
    BigDecimal getPrice();
}