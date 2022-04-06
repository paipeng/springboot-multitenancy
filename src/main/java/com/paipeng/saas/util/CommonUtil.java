package com.paipeng.saas.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class.getSimpleName());
    public static String getProjHomePath() {
        return System.getenv("PROJ_HOME");
    }

    public static String getSystemTime(String format) {
        if (format.isEmpty())
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static byte[] getByteArrayByImagePath(String imagePath) {
        if (imagePath == null || imagePath.equals("")) {
            log.warn("param is null");
            return null;
        }

        File file = new File(imagePath);
        if (file == null || file.exists() == false) {
            log.warn("file is not find");
            return null;
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(imagePath))) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int size = 0;
                byte[] temp = new byte[1024];
                while ((size = bufferedInputStream.read(temp)) != -1) {
                    out.write(temp, 0, size);
                }
                return out.toByteArray();
            }
        } catch (FileNotFoundException e) {
            log.error("getByteArrayByImagePath FileNotFoundException:" + e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("getByteArrayByImagePath IOException:" + e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("getByteArrayByImagePath Exception:" + e.getMessage());
            return null;
        }
    }

    public static String getBase64StringByByte(byte[] byteData) {
        try {
            String resultString = Base64Utils.encodeToString(byteData);
            return resultString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String generateJWTToken(String jwtSecret, String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        return Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512,
                        jwtSecret).compact();
    }
}
