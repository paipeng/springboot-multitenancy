package com.paipeng.saas.tenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.datasource.hikari.data-source-properties")
public class HikariDataSourceProperties {
    private boolean cachePrepStmts;
    private int prepStmtCacheSize;
    private int prepStmtCacheSqlLimit;
    private boolean useServerPrepStmts;
    private boolean useLocalSessionState;
    private boolean rewriteBatchedStatements;
    private boolean cacheResultSetMetadata;
    private boolean cacheServerConfiguration;
    private boolean elideSetAutoCommits;
    private boolean maintainTimeStats;
    private boolean showSql;
    private boolean formatSql;
    private String ddlAuto;
    private String dialect;

    public boolean isCachePrepStmts() {
        return cachePrepStmts;
    }

    public void setCachePrepStmts(boolean cachePrepStmts) {
        this.cachePrepStmts = cachePrepStmts;
    }

    public int getPrepStmtCacheSize() {
        return prepStmtCacheSize;
    }

    public void setPrepStmtCacheSize(int prepStmtCacheSize) {
        this.prepStmtCacheSize = prepStmtCacheSize;
    }

    public int getPrepStmtCacheSqlLimit() {
        return prepStmtCacheSqlLimit;
    }

    public void setPrepStmtCacheSqlLimit(int prepStmtCacheSqlLimit) {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
    }

    public boolean isUseServerPrepStmts() {
        return useServerPrepStmts;
    }

    public void setUseServerPrepStmts(boolean useServerPrepStmts) {
        this.useServerPrepStmts = useServerPrepStmts;
    }

    public boolean isUseLocalSessionState() {
        return useLocalSessionState;
    }

    public void setUseLocalSessionState(boolean useLocalSessionState) {
        this.useLocalSessionState = useLocalSessionState;
    }

    public boolean isRewriteBatchedStatements() {
        return rewriteBatchedStatements;
    }

    public void setRewriteBatchedStatements(boolean rewriteBatchedStatements) {
        this.rewriteBatchedStatements = rewriteBatchedStatements;
    }

    public boolean isCacheResultSetMetadata() {
        return cacheResultSetMetadata;
    }

    public void setCacheResultSetMetadata(boolean cacheResultSetMetadata) {
        this.cacheResultSetMetadata = cacheResultSetMetadata;
    }

    public boolean isCacheServerConfiguration() {
        return cacheServerConfiguration;
    }

    public void setCacheServerConfiguration(boolean cacheServerConfiguration) {
        this.cacheServerConfiguration = cacheServerConfiguration;
    }

    public boolean isElideSetAutoCommits() {
        return elideSetAutoCommits;
    }

    public void setElideSetAutoCommits(boolean elideSetAutoCommits) {
        this.elideSetAutoCommits = elideSetAutoCommits;
    }

    public boolean isMaintainTimeStats() {
        return maintainTimeStats;
    }

    public void setMaintainTimeStats(boolean maintainTimeStats) {
        this.maintainTimeStats = maintainTimeStats;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public boolean isFormatSql() {
        return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
