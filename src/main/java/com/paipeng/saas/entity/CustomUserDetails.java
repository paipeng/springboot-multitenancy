package com.paipeng.saas.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    /**
     * The extra field in the login form is for the tenant name
     */
    private String tenant;

    /**
     * Constructor based on the spring security User class but with an extra
     * argument <code>tenant</code> to store the tenant name submitted by the end
     * user.
     *
     * @param username
     * @param password
     * @param authorities
     * @param tenant
     */
    public CustomUserDetails(String username, String password,
                             Collection<? extends GrantedAuthority> authorities,
                             String tenant) {
        super(username, password, authorities);
        this.tenant = tenant;
    }

    // Getters and Setters
    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
