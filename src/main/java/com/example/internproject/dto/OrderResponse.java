package com.example.internproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.internproject.models.OrderItem;
import com.example.internproject.models.OrderStatus;

public class OrderResponse {

	private String id;
	private BigDecimal totalPrice;
	private OrderStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime pickupTime;
	private List<OrderItemResponse> orderItems;

	public OrderResponse(String id, BigDecimal totalPrice, OrderStatus status, LocalDateTime createdAt,
			LocalDateTime pickupTime, List<OrderItemResponse> orderItems) {

		this.id = id;
		this.totalPrice = totalPrice;
		this.status = status;
		this.createdAt = createdAt;
		this.pickupTime = pickupTime;
		this.orderItems = orderItems;
	}

	public String getOrderId() {
		return id;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public List<OrderItemResponse> getOrderItems() {
		return orderItems;
	}

}
