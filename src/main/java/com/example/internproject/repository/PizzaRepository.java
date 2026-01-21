package com.example.internproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.internproject.models.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {	
}
