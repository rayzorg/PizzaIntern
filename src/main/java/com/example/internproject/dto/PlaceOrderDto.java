package com.example.internproject.dto;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;

public class PlaceOrderDto {

	@NotEmpty
	@Valid
	private List<OrderItemRequestDto> items;

	@FutureOrPresent(message = "Pickup time cannot be in the past")
	private LocalDateTime pickupTime;

	@NotEmpty
	@Email(message = "Email must be valid")
	private String email;

	public PlaceOrderDto() {
	}

	public PlaceOrderDto(@NotEmpty @Valid List<OrderItemRequestDto> items,
			@FutureOrPresent(message = "Pickup time cannot be in the past") LocalDateTime pickupTime,
			@NotEmpty @Email(message = "Email must be valid") String email) {
		super();
		this.items = items;
		this.pickupTime = pickupTime;
		this.email = email;
	}

	public List<OrderItemRequestDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRequestDto> items) {
		this.items = items;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

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
