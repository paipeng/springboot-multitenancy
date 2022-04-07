/*
 * Copyright 2018 onwards - Sunit Katkar (sunitkatkar@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paipeng.saas.tenant.service;

import com.paipeng.saas.config.ApplicationConfig;
import com.paipeng.saas.security.AppAuthenticationToken;
import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.repository.UserRepository;
import com.paipeng.saas.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link UserService} which accesses the {@link User}
 * entity. This is the recommended way to access the entities through an
 * interface rather than using the corresponding repository. This allows for
 * separation into repository code and the service layer.
 *
 * @author Sunit Katkar, sunitkatkar@gmail.com
 * (https://sunitkatkar.blogspot.com/)
 * @version 1.0
 * @since ver 1.0 (May 2018)
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User justSavedUser = userRepository.save(user);
        logger.info("User:" + justSavedUser.getUsername() + " saved.");
        return justSavedUser;
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            String username = ((UserDetails) userDetails).getUsername();
            logger.info("Logged in username:" + username);
            return username;
        }
        return null;
    }

    @Override
    public User findByUsernameAndTenantname(String username, String tenant) {
        logger.info("findByUsernameAndTenantname: " + username + "tenant: " + tenant);
        User user = userRepository.findByUsernameAndTenantname(username,
                tenant);
        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format(
                            "Username not found for domain, "
                                    + "username=%s, tenant=%s",
                            username, tenant));
        }
        logger.info("Found user with username:" + user.getUsername()
                + " from tenant:" + user.getTenant());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User login(User user) throws Exception {
        logger.info("login: " + user.getUsername());
        logger.info("password: " + user.getPassword());
        logger.info("tenant: " + user.getTenant());

        if (user.getPassword() != null) {
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //String encodedPassword = passwordEncoder.encode(user.getPassword());
            //logger.trace("encodedPassword: " + encodedPassword);
            User foundUser = userRepository.findByUsernameAndTenantname(user.getUsername(), user.getTenant());
            logger.info("user found: " + foundUser.getId());
            if (foundUser != null) {
                if (CommonUtil.validatePassword(user.getPassword(), foundUser.getPassword())) {
                    String token = CommonUtil.generateJWTToken(applicationConfig.getSecurityJwtSecret(), foundUser);
                    logger.info("token: " + token);
                    foundUser.setToken(token);
                    logger.info("update user");
                    foundUser = userRepository.saveAndFlush(foundUser);
                    return foundUser;
                } else {
                    throw new Exception("401");
                }
            } else {
                throw new Exception("401");
            }
        }
        throw new Exception("401");
    }

    @Override
    public void logout() {
        AppAuthenticationToken auth = (AppAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
                logger.info("auth: " + grantedAuthority.getAuthority());
                logger.info("auth: " + grantedAuthority);
            }
            User user = (User) auth.getDetails();
            if (user != null) {
                logger.info("loginUser: " + user.getUsername());
                user.setToken(null);
                userRepository.saveAndFlush(user);
            }
        }
    }

    @Override
    public User register(User user) {
        return user;
    }

}
