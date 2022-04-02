package com.paipeng.saas.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

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
        this.tenant = tenant;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getTenant() {
        return this.tenant;
    }
}
