package com.paipeng.saas.security;

import com.paipeng.saas.tenant.model.CustomUserDetails;
import com.paipeng.saas.tenant.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AppAuthenticationToken extends AbstractAuthenticationToken {
    private final static Logger logger = LogManager.getLogger(AppAuthenticationToken.class.getSimpleName());

    private static final long serialVersionUID = 1L;

    /**
     * The tenant i.e. database identifier
     */
    private String tenant;
    CustomUserDetails customUserDetails;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public AppAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public AppAuthenticationToken(Object principal, Object credentials,
                                     String tenant, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        logger.info("CustomAuthenticationToken: " + tenant);
        this.tenant = tenant;

        if (authorities == null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            customUserDetails = new CustomUserDetails((String)principal, (String)credentials, grantedAuthorities, tenant);
        } else {
            customUserDetails = new CustomUserDetails((String)principal, (String)credentials, authorities, tenant);
        }

        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return customUserDetails;
    }

    @Override
    public String toString() {
        return AppAuthenticationToken.class.getSimpleName() + "\ntenent: " + tenant + "\n + detail: " + ((User)getDetails());
    }
}
