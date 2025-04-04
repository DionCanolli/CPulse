package com.dioncanolli.cpulse_back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "JWTTokenBlacklist")
public class JWTTokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwtId")
    private Long id;
    private String jwtValue;

    public JWTTokenBlacklist(String jwtValue) {
        this.jwtValue = jwtValue;
    }

    public JWTTokenBlacklist() {
    }
}