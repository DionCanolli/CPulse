package com.dioncanolli.cpulse_back_end.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Products")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Long id;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productStockQuantity;
    private String productImageUrl;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Product() {
    }

    public Product(String productName, String productDescription, double productPrice, int productStockQuantity, String productImageUrl, Category category) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
        this.productImageUrl = productImageUrl;
        this.category = category;
    }

    public Product(Long id, String productName, String productDescription, double productPrice, int productStockQuantity, String productImageUrl, Category category) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
        this.productImageUrl = productImageUrl;
        this.category = category;
    }
}
