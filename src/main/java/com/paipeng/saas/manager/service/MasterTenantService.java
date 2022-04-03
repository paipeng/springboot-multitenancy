package com.paipeng.saas.manager.service;

import com.paipeng.saas.manager.entity.MasterTenant;
import com.paipeng.saas.manager.repository.MasterTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterTenantService {
    @Autowired
    MasterTenantRepository masterTenantRepo;

    public MasterTenant findByTenantId(String tenantId) {
        return masterTenantRepo.findByTenantId(tenantId);
    }
}
