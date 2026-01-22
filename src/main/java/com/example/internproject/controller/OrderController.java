package com.example.internproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.internproject.dto.CreateOrder;
import com.example.internproject.dto.OrderResponse;
import com.example.internproject.dto.OrderSummaryDto;
import com.example.internproject.models.Orders;
import com.example.internproject.models.User;
import com.example.internproject.repository.OrderRepository;
import com.example.internproject.services.OrderService;
import com.example.internproject.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService,UserService userService) {
        this.orderService = orderService;
        this.userService=userService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
        @Valid @RequestBody CreateOrder dto,@AuthenticationPrincipal User user
    ) {
    	String email = (user != null) ? user.getEmail() : null;
        OrderResponse response = orderService.placeOrder(dto,email);

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderSummaryDto>> getOrderHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/myorders")
    public List<OrderSummaryDto> getMyOrders(Authentication authentication) {	
    	 User user = (User) authentication.getPrincipal();
    	 
        return orderService.getOrderHistory(user.getId());
    }

  
    
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

}
