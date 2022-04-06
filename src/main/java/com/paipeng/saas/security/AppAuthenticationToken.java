package com.paipeng.saas.security;

import com.paipeng.saas.tenant.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppAuthenticationToken extends AbstractAuthenticationToken {
    private final static Logger logger = LogManager.getLogger(AppAuthenticationToken.class.getSimpleName());

    private static final long serialVersionUID = 1L;

    /**
     * The tenant i.e. database identifier
     */
    private String tenant;
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
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public String toString() {
        return AppAuthenticationToken.class.getSimpleName() + "\ntenent: " + tenant + "\n + detail: " + ((User)getDetails());
    }
}
