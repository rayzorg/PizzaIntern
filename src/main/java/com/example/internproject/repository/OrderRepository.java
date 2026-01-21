package com.example.internproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.internproject.models.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserIdOrderByCreatedAtDesc(Long userId);
}
