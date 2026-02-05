package com.example.internproject.dto;

import java.math.BigDecimal;

public class OrderItemSummaryDto {

	 private String pizzaName;
	    private String size;
	    private int quantity;
	    private BigDecimal price;

	    public OrderItemSummaryDto(
	            String pizzaName,
	            String size,
	            int quantity,
	            BigDecimal price) {
	        this.pizzaName = pizzaName;
	        this.size = size;
	        this.quantity = quantity;
	        this.price = price;
	    }

	    public String getPizzaName() {
	        return pizzaName;
	    }

	    public String getSize() {
	        return size;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public BigDecimal getPrice() {
	        return price;
	    }

		
	    
}
