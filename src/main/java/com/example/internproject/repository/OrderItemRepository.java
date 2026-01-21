package com.example.internproject.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.internproject.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

