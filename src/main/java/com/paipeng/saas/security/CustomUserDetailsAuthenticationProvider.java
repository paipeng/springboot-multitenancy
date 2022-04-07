package com.paipeng.saas.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final static Logger logger = LogManager.getLogger(CustomUserDetailsAuthenticationProvider.class.getSimpleName());

    /**
     * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
     * String)} on when the user is not found to avoid SEC-2056
     * (https://github.com/spring-projects/spring-security/issues/2280).
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    /**
     * For encoding and/or matching the encrypted password stored in the database
     * with the user submitted password
     */
    private PasswordEncoder passwordEncoder;

    private CustomUserDetailsService userDetailsService;

    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is not
     * found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is
     * not in a valid format.
     */
    private String userNotFoundEncodedPassword;

    public CustomUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                   CustomUserDetailsService userDetailsService) {
        logger.info("CustomUserDetailsAuthenticationProvider");
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("authenticate paipeng");
        return super.authenticate(authentication);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.authentication.dao.
     * AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.
     * springframework.security.core.userdetails.UserDetails,
     * org.springframework.security.authentication.
     * UsernamePasswordAuthenticationToken)
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        logger.info("additionalAuthenticationChecks");
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
        // Get the password submitted by the end user
        String presentedPassword = authentication.getCredentials().toString();

        // If the password stored in the database and the user submitted password do not
        // match, then signal a login error
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        logger.info("additionalAuthenticationChecks");
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.authentication.dao.
     * AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
     * org.springframework.security.authentication.
     * UsernamePasswordAuthenticationToken)
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        logger.info("retrieveUser");

        //CustomAuthenticationToken auth = (CustomAuthenticationToken) authentication;
        UserDetails loadedUser = null;

        try {
            //loadedUser = this.userDetailsService
            //        .loadUserByUsernameAndTenantname(auth.getPrincipal().toString(),
            //                auth.getTenant());
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(),
                    repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, "
                            + "which is an interface contract violation");
        }

        return loadedUser;
    }
}
