package com.paipeng.saas.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;

@Entity
@Table(name = "authorizations")
public class Authorization extends BaseEntity {

    @Column(name = "activate", columnDefinition = "bit default 0 ", nullable = false)
    private boolean activate;

    @Column(name = "file_path", nullable = true, length = 128)
    private String filePath;

    @Transient
    private String imageBase64;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @LazyToOne(value = LazyToOneOption.FALSE)
    private Product product;

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
