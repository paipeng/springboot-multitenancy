package com.paipeng.saas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("multitenancy.mtapp")
public class MultitenancyConfig {
    private List<DataSourceProperties> dataSourcesProps;

    public List<DataSourceProperties> getDataSources() {
        return this.dataSourcesProps;
    }

    public void setDataSources(List<DataSourceProperties> dataSourcesProps) {
        this.dataSourcesProps = dataSourcesProps;
    }

    public static class DataSourceProperties
            extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

        private String tenantId;

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
    }
}
