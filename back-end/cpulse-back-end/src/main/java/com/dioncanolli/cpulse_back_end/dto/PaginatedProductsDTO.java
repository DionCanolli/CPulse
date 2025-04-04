package com.dioncanolli.cpulse_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedProductsDTO {
    private List<ProductDTO> allProducts;
    private long totalProducts;
}
