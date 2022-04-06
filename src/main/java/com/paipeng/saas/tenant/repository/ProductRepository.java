package com.paipeng.saas.tenant.repository;

import com.paipeng.saas.tenant.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcodeData);
}
