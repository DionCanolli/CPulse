package com.dioncanolli.cpulse_back_end.repository;

import com.dioncanolli.cpulse_back_end.entity.Payment;
import com.dioncanolli.cpulse_back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser(User user);
    void deleteAllByUser(User user);
}
