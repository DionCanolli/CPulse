package com.dioncanolli.cpulse_back_end.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Wishlist")
@Getter
@Setter
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlistItemId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    public WishlistItem(Long id, User user, Product product) {
        this.id = id;
        this.user = user;
        this.product = product;
    }

    public WishlistItem(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public WishlistItem() {
    }
}
