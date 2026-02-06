package com.example.internproject.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {

	private Long pizzaId;
	private String pizzaName;
	private String size;
	private int quantity;
	private BigDecimal price;

	public OrderItemResponseDto() {
	}

	public OrderItemResponseDto(Long pizzaId, String pizzaName, String size, int quantity, BigDecimal price) {

		this.pizzaId = pizzaId;
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

	public Long getPizzaId() {
		return pizzaId;
	}

	
}
