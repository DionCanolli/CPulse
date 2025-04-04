package com.dioncanolli.cpulse_back_end.repository;

import com.dioncanolli.cpulse_back_end.entity.Category;
import com.dioncanolli.cpulse_back_end.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllProductsByCategory(Category category, Pageable pageable);

    Page<Product> findAllProductsByProductNameIgnoreCase(String productName, Pageable pageable);

    Product findProductByProductName(String productName);

    Page<Product> findAllProductsByCategoryAndProductNameIgnoreCase(Category category, String productName, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
