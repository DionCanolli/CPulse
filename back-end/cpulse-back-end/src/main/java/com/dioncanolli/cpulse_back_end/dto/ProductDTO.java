package com.dioncanolli.cpulse_back_end.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private String productImageUrl;
    private double productPrice;
    private int productStockQuantity;

    public ProductDTO(Long productId, String productName, String productDescription, String productCategory, String productImageUrl, double productPrice, int productStockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
    }

    public ProductDTO(String productName, String productDescription, String productCategory,  double productPrice, int productStockQuantity, String productImageUrl) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
    }

    public ProductDTO(Long productId, String productName, String productDescription, String productCategory, double productPrice, int productStockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
    }

    public ProductDTO(Long productId, String productName, String productDescription, double productPrice, int productStockQuantity, String productImageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
        this.productImageUrl = productImageUrl;
    }

    public ProductDTO() {
    }
}
