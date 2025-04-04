package com.dioncanolli.cpulse_back_end.utility;

import com.dioncanolli.cpulse_back_end.dto.*;
import com.dioncanolli.cpulse_back_end.entity.*;
import com.dioncanolli.cpulse_back_end.service.MyService;


public class ClassMapper {

    private final MyService myService;

    public ClassMapper(MyService myService) {
        this.myService = myService;
    }

    public static CartDTO mapToCartDTO(CartItem cartItem){
        return CartDTO.builder()
                .username(cartItem.getUser().getUsername())
                .email(cartItem.getUser().getEmail())
                .productName(cartItem.getProduct().getProductName())
                .productCategory(cartItem.getProduct().getCategory().getCategoryName())
                .productPrice(cartItem.getProduct().getProductPrice())
                .build();
    }

    public static ProductDTO mapToProductDTO(Product product){
        return ProductDTO.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productCategory(product.getCategory().getCategoryName())
                .productImageUrl(product.getProductImageUrl())
                .productPrice(product.getProductPrice())
                .productStockQuantity(product.getProductStockQuantity())
                .build();
    }

    public static UserDTO mapToUserDTO(User user){
        return UserDTO.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static WishlistDTO mapToWishlistDTO(WishlistItem wishlistItem){
        return WishlistDTO.builder()
                .username(wishlistItem.getUser().getUsername())
                .email(wishlistItem.getUser().getEmail())
                .productName(wishlistItem.getProduct().getProductName())
                .productCategory(wishlistItem.getProduct().getCategory().getCategoryName())
                .productPrice(wishlistItem.getProduct().getProductPrice())
                .build();
    }
}





























