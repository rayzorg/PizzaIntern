package com.example.internproject.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.internproject.dto.PizzaResponseDto;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Topping;
import com.example.internproject.repository.PizzaRepository;

import jakarta.transaction.Transactional;

@Service
public class PizzaService {

	private final PizzaRepository pizzaRepository;
	
	
	
	public PizzaService(PizzaRepository pizzaRepository) {
		this.pizzaRepository=pizzaRepository;
	}
	
	
	public List<PizzaResponseDto> getAll() {
        return pizzaRepository.findAll()
                .stream()
                .map(this::toDto)   
                .toList();
    }
	 private PizzaResponseDto toDto(Pizza pizza) {
	        return new PizzaResponseDto(
	            pizza.getId(),
	            pizza.getName(),
	            pizza.getDescription(),
	            pizza.isAvailable(),
	            pizza.getBasePrice(),
	            pizza.getImageUrl(), 
	            pizza.getToppings()
	                 .stream()
	                 .map(Topping::getName)
	                 .collect(Collectors.toSet())
	        );
	    }
	
    public Pizza addPizza(Pizza pizza) { return pizzaRepository.save(pizza); }
    
    

    @Transactional
    public void updateAvailability(Long id, boolean available) {
        Pizza pizza = pizzaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pizza not found"));

        pizza.setAvailable(available);
    }

	
}
