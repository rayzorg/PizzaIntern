package com.example.internproject.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.internproject.dto.PlaceOrderDto;
import com.example.internproject.dto.OrderPlacedResponseDto;
import com.example.internproject.dto.OrderSummaryCustomerDto;
import com.example.internproject.models.User;
import com.example.internproject.security.UserPrincipal;
import com.example.internproject.services.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<OrderPlacedResponseDto> placeOrder(@Valid @RequestBody PlaceOrderDto dto,
			@AuthenticationPrincipal UserPrincipal principal) {
		dto.setEmail(principal.getUsername());
		OrderPlacedResponseDto response = orderService.placeOrder(dto, principal.getUsername());

		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/myorders")
	public List<OrderSummaryCustomerDto> getMyOrders(@AuthenticationPrincipal UserPrincipal principal) {
		return orderService.getOrderHistoryForCustomer(principal.getId());
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/{publicId}")
	public OrderPlacedResponseDto getOrderWhenPlaced(@PathVariable String publicId,
			@AuthenticationPrincipal UserPrincipal user) {
		return orderService.getOrderWhenPlaced(publicId, user);
	}

}
