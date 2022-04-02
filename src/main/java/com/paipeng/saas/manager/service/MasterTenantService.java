package com.paipeng.saas.manager.service;

import com.paipeng.saas.manager.entity.MasterTenant;
import org.springframework.data.repository.query.Param;
public interface MasterTenantService {
    MasterTenant findByTenantId(@Param("tenantId") String tenantId);
}
