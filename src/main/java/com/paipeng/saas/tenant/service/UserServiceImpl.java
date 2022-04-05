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

import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User login(User user) {
        //logger.info("my login: " + user.getEmail());
        logger.info("my password: " + user.getPassword());
        return user;
    }

    @Override
    public User register(User user) {
        return user;
    }

    @Override
    public void logout() {

    }
}
