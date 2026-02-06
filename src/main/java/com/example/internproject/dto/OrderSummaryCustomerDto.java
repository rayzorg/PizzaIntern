package com.example.internproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.internproject.models.OrderStatus;

public class OrderSummaryCustomerDto {

	    private String publicId;
	    private LocalDateTime createdAt;
	    private BigDecimal totalPrice;
	    private OrderStatus status;
	    private List<OrderItemSummaryDto> orderItems;

	    public OrderSummaryCustomerDto(
	            String publicId,
	            LocalDateTime createdAt,
	            BigDecimal totalPrice,
	            OrderStatus status,
	            List<OrderItemSummaryDto> orderItems) {
	        this.publicId = publicId;
	        this.createdAt = createdAt;
	        this.totalPrice = totalPrice;
	        this.status = status;
	        this.orderItems = orderItems;
	    }

	    public String getPublicId() {
	        return publicId;
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
