package com.paipeng.saas.repository;

import com.paipeng.saas.entity.Authorization;
import com.paipeng.saas.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {
    List<Authorization> findAllByProduct(Product product);
}
