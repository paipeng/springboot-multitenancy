package com.paipeng.saas.security;

import com.paipeng.saas.entity.CustomUserDetails;
import com.paipeng.saas.entity.Role;
import com.paipeng.saas.entity.User;
import com.paipeng.saas.repository.UserRepository;
import com.paipeng.saas.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
public class CustomUserDetailsService {

    @Autowired
    private UserRepository userRepository;


    public UserDetails loadUserByUsernameAndTenantname(String username, String tenant)
            throws UsernameNotFoundException {
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
}
