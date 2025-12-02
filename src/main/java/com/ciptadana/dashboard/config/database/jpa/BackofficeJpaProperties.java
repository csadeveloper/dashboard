package com.ciptadana.dashboard.config.database.jpa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.backoffice.jpa")
public class BackofficeJpaProperties {
    private HibernateProperties hibernateProperties;
    private boolean showSql;
    private boolean openInView;
}
