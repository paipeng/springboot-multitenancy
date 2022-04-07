package com.paipeng.saas.controller;
import com.paipeng.saas.config.VersionConfig;
import com.paipeng.saas.security.AppAuthenticationToken;
import com.paipeng.saas.tenant.config.HikariConfigProperties;
import com.paipeng.saas.tenant.service.UserService;
import com.paipeng.saas.util.AvailableTenantsInformationHolder;
import com.paipeng.saas.util.TenantContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class VersionController {
    private final static Logger logger = LogManager.getLogger(VersionController.class.getSimpleName());

    @Autowired
    private VersionConfig versionConfig;

    @Autowired
    private HikariConfigProperties hikariConfigProperties;

    @Autowired
    private UserService userService;

    @GetMapping("/version")
    public Map<String, String> version() {
        //log.trace("version");
        Map<String, String> versionMap = new HashMap<>();
        versionMap.put("name", versionConfig.getName());
        versionMap.put("version", versionConfig.getVersion());
        versionMap.put("time", stampToDate(Calendar.getInstance().getTimeInMillis()));
        versionMap.put("sha256", getSha256String());
        //versionMap.put("tomcat", System.getProperty("catalina.base") + "/webapps/");
        versionMap.put("createData", versionConfig.getCreateData());
        versionMap.put("currentDir", System.getenv("PROJ_HOME"));

        versionMap.put("currentUser", ((AppAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).toString());


        logger.info(hikariConfigProperties);
        return versionMap;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        logger.info("doSomethingAfterStartup");
        for (String key : AvailableTenantsInformationHolder.getAvailableTenants().keySet()) {
            logger.info(key + ":" + key + " datasource: " + AvailableTenantsInformationHolder.getAvailableTenants().get(key));
            TenantContextHolder.setTenantId(key);
            userService.findAllUsers();
        }
    }

    private String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    private String getSha256String() {
        String result = "";
        InputStream in;

        try {
            Process pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "sha256sum " + System.getenv("PROJ_HOME") + "/libs/idcard.jar"});
            pro.waitFor();
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            result = read.readLine();
            if (result == null) {
                result = "";
            } else {
                result = result.split(" ")[0];
            }
        } catch (Exception e) {
            //log.error(e.getMessage());
        }

        return result;
    }

}

