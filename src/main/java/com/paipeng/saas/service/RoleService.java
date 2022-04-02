package com.paipeng.saas.service;

import com.paipeng.saas.entity.Role;
import com.paipeng.saas.repository.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService{
    private final static Logger logger = LogManager.getLogger(UserService.class.getSimpleName());
    @Autowired
    private RoleRepository roleRepository;

    public Role findByRole(String roleName) {
        Role role = roleRepository.findByRole(roleName);
        logger.info("Role:" + role.getRole() + " found");
        return role;
    }
}
