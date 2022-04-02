package com.paipeng.saas.controller;

import com.paipeng.saas.entity.Product;
import com.paipeng.saas.service.ProductService;
import com.sun.istack.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    private final static Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());
    @Autowired
    private ProductService productService;

    @GetMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public List<Product> getAll() throws Exception {
        logger.info("getAll");
        return productService.getProducts();
    }

    @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
    public Product getProductById(@NotNull @PathVariable("id") Long id, HttpServletResponse httpServletResponse) {
        logger.info("getProductById: " + id);
        Product product = productService.getProductById(id);
        if (product == null) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return product;
    }


    @PostMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public Product save(@RequestBody Product product, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return productService.save(product);
    }


    @GetMapping(value = "/barcodes/{data}", produces = {"application/json;charset=UTF-8"})
    public Product getProductByBarcode(@NotNull @PathVariable("data") String barcodeData, HttpServletResponse httpServletResponse) {
        logger.info("getProductByBarcode: " + barcodeData);
        Product product = productService.getProductByBarcode(barcodeData);
        if (product == null) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return product;
    }
}
