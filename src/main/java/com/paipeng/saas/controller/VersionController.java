package com.paipeng.saas.controller;

import com.paipeng.saas.config.VersionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //private final Logger log;

    @Autowired
    private VersionConfig versionConfig;

    public VersionController() {
        //this.log = LoggerFactory.getLogger(this.getClass().getName());
    }

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

        return versionMap;
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
