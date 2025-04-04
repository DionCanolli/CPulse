package com.dioncanolli.cpulse_back_end.repository;

import com.dioncanolli.cpulse_back_end.entity.CartItem;
import com.dioncanolli.cpulse_back_end.entity.Product;
import com.dioncanolli.cpulse_back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllCartItemsByUser(User user);
    CartItem findByUserAndProduct(User user, Product product);
    void deleteAllByUser(User user);
}
