package com.paipeng.saas.util;

import javax.sql.DataSource;

import com.paipeng.saas.master.model.MasterTenant;
import com.paipeng.saas.tenant.config.HikariConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

public class DataSourceUtil {
    private static final Logger LOG = LoggerFactory
            .getLogger(DataSourceUtil.class);

    /**
     * Utility method to create and configure a data source
     *
     * @param masterTenant
     * @return
     */
    public static DataSource createAndConfigureDataSource(MasterTenant masterTenant, HikariConfigProperties hikariConfigProperties) {
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(masterTenant.getUsername());
        ds.setPassword(masterTenant.getPassword());
        ds.setJdbcUrl(masterTenant.getUrl());
        //ds.setDataSourceClassName(hikariConfigProperties.getDataSourceClassName());
        ds.setDriverClassName(hikariConfigProperties.getDriverClassName());

        // HikariCP settings - could come from the master_tenant table but
        // hardcoded here for brevity
        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(hikariConfigProperties.getConnectionTimeout());

        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(hikariConfigProperties.getMinimumIdle());

        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(hikariConfigProperties.getMaximumPoolSize());

        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(hikariConfigProperties.getIdleTimeout());
        ds.setAutoCommit(hikariConfigProperties.isAutoCommit());
        ds.setReadOnly(false);
        Properties properties = new Properties();
        properties.setProperty("cachePrepStmts", "true");
        properties.setProperty("prepStmtCacheSize", "250");
        properties.setProperty("prepStmtCacheSqlLimit", "2048");
        properties.setProperty("useServerPrepStmts", "true");
        properties.setProperty("useLocalSessionState", "true");
        properties.setProperty("useLocalTransactionState", "true");

        properties.setProperty("rewriteBatchedStatements", "true");

        properties.setProperty("cacheResultSetMetadata", "true");
        properties.setProperty("cacheServerConfiguration", "true");
        properties.setProperty("elideSetAutoCommits", "true");
        properties.setProperty("maintainTimeStats", "false");
        properties.setProperty("useUnicode","true");


        ds.setDataSourceProperties(properties);

        // Setting up a pool name for each tenant datasource
        String tenantId = masterTenant.getTenantId();
        String tenantConnectionPoolName = tenantId + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        LOG.info("Configured datasource:" + masterTenant.getTenantId()
                + ". Connection poolname:" + tenantConnectionPoolName);
        return ds;
    }
}
