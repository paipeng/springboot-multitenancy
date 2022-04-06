package com.paipeng.saas.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final static Logger logger = LogManager.getLogger(CustomAuthenticationToken.class.getSimpleName());

    private static final long serialVersionUID = 1L;

    /**
     * The tenant i.e. database identifier
     */
    private String tenant;

    /**
     * @param principal
     * @param credentials
     * @param tenant
     */
    public CustomAuthenticationToken(Object principal, Object credentials,
                                     String tenant) {

        super(principal, credentials);
        logger.info("CustomAuthenticationToken: " + tenant);
        this.tenant = tenant;
        super.setAuthenticated(false);
    }

    /**
     * @param principal
     * @param credentials
     * @param tenant
     * @param authorities
     */
    public CustomAuthenticationToken(Object principal, Object credentials,
                                     String tenant, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);

        logger.info("CustomAuthenticationToken: " + tenant);
        this.tenant = tenant;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getTenant() {
        return this.tenant;
    }
}
