package com.paipeng.saas.tenant.service;

import com.paipeng.saas.security.AppAuthenticationToken;
import com.paipeng.saas.tenant.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {
    protected User getUserFromSecurity() {
        AppAuthenticationToken auth = (AppAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return  (User) auth.getDetails();
        } else {
            return null;
        }
    }
}
