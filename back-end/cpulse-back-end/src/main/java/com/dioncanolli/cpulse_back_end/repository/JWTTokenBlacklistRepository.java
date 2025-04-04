package com.dioncanolli.cpulse_back_end.repository;

import com.dioncanolli.cpulse_back_end.entity.JWTTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTTokenBlacklistRepository extends JpaRepository<JWTTokenBlacklist, Long> {

    JWTTokenBlacklist findByJwtValue(String value);
}
