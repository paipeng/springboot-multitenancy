package com.paipeng.saas.repository;

import com.paipeng.saas.entity.Authorization;
import com.paipeng.saas.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {
    List<Authorization> findAllByProduct(Product product);
}
