package com.paipeng.saas.security;

import com.paipeng.saas.tenant.model.CustomUserDetails;
import com.paipeng.saas.tenant.model.Role;
import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final static Logger logger = LogManager.getLogger(CustomUserDetailsService.class.getSimpleName());

    @Autowired
    private UserRepository userRepository;


    public UserDetails loadUserByUsernameAndTenantname(String username, String tenant)
            throws UsernameNotFoundException {
        logger.info("loadUserByUsernameAndTenantname: " + tenant);

        if (StringUtils.isAnyBlank(username, tenant)) {
            throw new UsernameNotFoundException("Username and domain must be provided");
        }
        // Look for the user based on the username and tenant by accessing the
        // UserRepository via the UserService
        User user = userRepository.findByUsernameAndTenantname(username, tenant);

        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("Username not found for domain, "
                            + "username=%s, tenant=%s", username, tenant));
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        CustomUserDetails customUserDetails =
                new CustomUserDetails(user.getUsername(),
                        user.getPassword(), grantedAuthorities, tenant);

        return customUserDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
