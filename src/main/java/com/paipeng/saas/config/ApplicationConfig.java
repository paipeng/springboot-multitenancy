package com.paipeng.saas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ConfigurationProperties()
public class ApplicationConfig {
    @Value("${security.jwt.secret}")
    private String securityJwtSecret;

    @Bean
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/string");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }

    public String getSecurityJwtSecret() {
        return securityJwtSecret;
    }

}
