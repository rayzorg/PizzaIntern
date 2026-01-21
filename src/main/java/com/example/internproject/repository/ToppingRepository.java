package com.example.internproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.internproject.models.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
}
