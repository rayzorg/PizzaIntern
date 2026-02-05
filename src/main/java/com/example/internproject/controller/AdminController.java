package com.example.internproject.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.internproject.dto.OrderResponse;
import com.example.internproject.services.OrderService;
import com.example.internproject.services.PizzaService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class AdminController {

	private final OrderService orderService;
	private final PizzaService pizzaService;

	public AdminController(OrderService orderService, PizzaService pizzaService) {
		this.orderService = orderService;
		this.pizzaService = pizzaService;
	}

	@GetMapping("/orders")
	public List<OrderResponse> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PutMapping("orders/{id}/close")
	public OrderResponse closeOrder(@PathVariable String id) {
		return orderService.closeOrder(id);
	}

	@PutMapping("/pizzas/{id}/availability")
	public void updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
		pizzaService.updateAvailability(id, available);
	}

}
