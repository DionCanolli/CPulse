package com.dioncanolli.cpulse_back_end.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private Long id;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @CreationTimestamp
    private Timestamp paymentDate;

    public Payment(Long id, double amount, User user, Timestamp paymentDate) {
        this.id = id;
        this.amount = amount;
        this.user = user;
        this.paymentDate = paymentDate;
    }

    public Payment(double amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public Payment() {
    }
}
