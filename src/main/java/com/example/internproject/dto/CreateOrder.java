package com.example.internproject.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateOrder {

	    
	    @NotEmpty
	    @Valid
	    private List<OrderItemRequest> items;
	    
	    @FutureOrPresent(message = "Pickup time cannot be in the past")
	    private LocalDateTime pickupTime; // new field
	    
	    @NotEmpty
	    @Email(message="Email must be valid")
	    private String email;

	    public CreateOrder() {}

		public List<OrderItemRequest> getItems() {
			return items;
		}

		public void setItems(List<OrderItemRequest> items) {
			this.items = items;
		}
	    public LocalDateTime getPickupTime() { return pickupTime; }

		public void setPickupTime(LocalDateTime pickupTime) {
			this.pickupTime = pickupTime;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		
	    
}
