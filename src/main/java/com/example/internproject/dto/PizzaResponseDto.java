package com.example.internproject.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.example.internproject.models.Pizza;
import com.example.internproject.models.Topping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class PizzaResponseDto {
	
	

    public Long pizzaId;
    
    public String name;
    
    public BigDecimal price;
    private boolean available;
    private String imageUrl;

    private String description;
    private Set<String> toppings;

    
	public PizzaResponseDto(Long pizzaId, String name,String description,boolean available, BigDecimal price,String imageUrl, Set<String> toppings) {
		this.pizzaId = pizzaId;
		this.name = name;
		this.description=description;
		this.available=available;
		this.price = price;
		this.imageUrl=imageUrl;
        this.toppings = toppings;
	}
    
	public Long getId() { return pizzaId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public Set<String> getToppings() { return toppings; }

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isAvailable() {
		return available;
	}

	

}
