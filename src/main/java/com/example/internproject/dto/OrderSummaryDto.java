package com.example.internproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Size;

public class OrderSummaryDto {

	 private Long orderId;
	    private LocalDateTime createdAt;
	    private BigDecimal totalPrice;
	    private OrderStatus status;
	    private List<OrderItemSummaryDto> orderItems;

	    public OrderSummaryDto(
	            Long orderId,
	            LocalDateTime createdAt,
	            BigDecimal totalPrice,
	            OrderStatus status,
	            List<OrderItemSummaryDto> orderItems) {
	        this.orderId = orderId;
	        this.createdAt = createdAt;
	        this.totalPrice = totalPrice;
	        this.status = status;
	        this.orderItems = orderItems;
	    }

	    public Long getOrderId() {
	        return orderId;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public BigDecimal getTotalPrice() {
	        return totalPrice;
	    }

	    public OrderStatus getStatus() {
	        return status;
	    }

	    public List<OrderItemSummaryDto> getItems() {
	        return orderItems;
	    }
	}
