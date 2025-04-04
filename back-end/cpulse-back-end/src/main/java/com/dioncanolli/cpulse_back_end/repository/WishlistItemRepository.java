package com.dioncanolli.cpulse_back_end.repository;

import com.dioncanolli.cpulse_back_end.entity.Product;
import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findAllWishlistItemsByUser(User user);
    WishlistItem findByUserAndProduct(User user, Product product);
    void deleteAllByUser(User user);
}
