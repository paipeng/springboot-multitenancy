package com.paipeng.saas.util;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AvailableTenantsInformationHolder {
    private static Map<String, DataSource> dataSourcesMtApp = new TreeMap<>();

    public static void put(String key, DataSource dataSource) {
        dataSourcesMtApp.put(key,dataSource);
    }

    public static Map<String, DataSource> getAvailableTenants() {
        return dataSourcesMtApp;
    }

    public static DataSource getFirstDataSource() {
        return dataSourcesMtApp.values().iterator().next();
    }
}
