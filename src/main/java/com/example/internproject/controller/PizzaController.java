package com.example.internproject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.internproject.dto.PizzaResponseDto;
import com.example.internproject.models.Pizza;
import com.example.internproject.services.PizzaService;

@RestController
@RequestMapping("/api/pizzas")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService service) {
    	this.pizzaService=service;
    }
    @GetMapping
    public List<PizzaResponseDto> getAllPizzas() {
    	return pizzaService.getAll();  
       
    }
}