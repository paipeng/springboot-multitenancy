package com.paipeng.saas.tenant.service;

import com.paipeng.saas.config.ApplicationConfig;
import com.paipeng.saas.tenant.model.Product;
import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService extends BaseService {
    private final static Logger logger = LogManager.getLogger(ProductService.class.getSimpleName());
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationConfig applicationConfig;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "tenantTransactionManager")
    public Product save(Product product) throws Exception {
        User currentUser = getUserFromSecurity();
        if (currentUser == null) {
            throw new Exception("403");
        }

        if (productRepository.findByBarcode(product.getBarcode()).orElse(null) != null) {
            throw new Exception("409");
        }

        logger.info("setUser to current User again");
        product.setUser(currentUser);

        logger.info("save product");
        product = productRepository.saveAndFlush(product);
        logger.info("save done");
        return product;
    }

    public Product getProductByBarcode(String barcodeData) {
        return productRepository.findByBarcode(barcodeData).orElse(null);
    }
}
