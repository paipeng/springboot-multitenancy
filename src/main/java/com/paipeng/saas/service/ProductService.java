package com.paipeng.saas.service;

import com.paipeng.saas.config.ApplicationConfig;
import com.paipeng.saas.entity.Product;
import com.paipeng.saas.entity.User;
import com.paipeng.saas.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Product save(Product product) throws Exception {
        User currentUser = getUserFromSecurity();
        if (currentUser == null) {
            throw new Exception("403");
        }

        if (productRepository.findByBarcode(product.getBarcode()).orElse(null) != null) {
            throw new Exception("409");
        }

        product.setUser(currentUser);

        product = productRepository.saveAndFlush(product);
        return product;
    }

    public Product getProductByBarcode(String barcodeData) {
        return productRepository.findByBarcode(barcodeData).orElse(null);
    }
}
