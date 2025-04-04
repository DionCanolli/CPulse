package com.dioncanolli.cpulse_back_end.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Cart")
@Getter
@Setter
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    public CartItem(Long id, User user, Product product) {
        this.id = id;
        this.user = user;
        this.product = product;
    }

    public CartItem(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public CartItem() {
    }
}
