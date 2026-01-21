package com.example.internproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.internproject.models.OrderItem;
import com.example.internproject.models.OrderStatus;

public class OrderResponse {


	    private Long orderId;
	    private BigDecimal totalPrice;
	    private OrderStatus status;
	    private LocalDateTime createdAt;
	    private LocalDateTime pickupTime; 
	    private List<OrderItemResponse> orderItems;
	    

	    
		public OrderResponse(Long orderId, BigDecimal totalPrice, OrderStatus status, LocalDateTime createdAt, LocalDateTime pickupTime,List<OrderItemResponse> orderItems) {
			
			this.orderId = orderId;
			this.totalPrice = totalPrice;
			this.status = status;
			this.createdAt = createdAt;
			this.pickupTime=pickupTime;
			this.orderItems=orderItems;
		}

		public Long getOrderId() {
			return orderId;
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
