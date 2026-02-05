package com.example.internproject.dto;

import com.example.internproject.models.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequest {
	

	@NotNull
	private Long pizzaId;
	@NotNull
    private Size size;
	@Min(1)
    private int quantity;

   public OrderItemRequest() {
	   
   }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	    
	}


