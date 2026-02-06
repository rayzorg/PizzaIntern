package com.example.internproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.internproject.models.OrderStatus;

public class OrderPlacedResponseDto {

	private String publicId;
	private String email;
	private BigDecimal totalPrice;
	private OrderStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime pickupTime;
	private List<OrderItemResponseDto> orderItems;

	public OrderPlacedResponseDto(String publicId,String email, BigDecimal totalPrice, OrderStatus status, LocalDateTime createdAt,
			LocalDateTime pickupTime, List<OrderItemResponseDto> orderItems) {

		this.publicId = publicId;
		this.email=email;
		this.totalPrice = totalPrice;
		this.status = status;
		this.createdAt = createdAt;
		this.pickupTime = pickupTime;
		this.orderItems = orderItems;
	}

	public String getPublicId() {
		return publicId;
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

	public List<OrderItemResponseDto> getOrderItems() {
		return orderItems;
	}
	public String getEmail() {
		return email;
	}

}
