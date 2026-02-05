package com.example.internproject.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.internproject.dto.CreateOrder;
import com.example.internproject.dto.OrderItemRequest;
import com.example.internproject.dto.OrderItemResponse;
import com.example.internproject.dto.OrderItemSummaryDto;
import com.example.internproject.dto.OrderResponse;
import com.example.internproject.dto.OrderSummaryDto;
import com.example.internproject.models.OrderItem;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Orders;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.User;
import com.example.internproject.repository.OrderRepository;
import com.example.internproject.repository.PizzaRepository;
import com.example.internproject.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional // If anything fails → rollback.
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final PizzaRepository pizzaRepository;
	private final EmailService emailService;

	public OrderService(OrderRepository orderRepository, UserRepository userRepository, PizzaRepository pizzaRepository,
			EmailService emailService) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.pizzaRepository = pizzaRepository;
		this.emailService = emailService;
	}

	@Transactional
	public OrderResponse placeOrder(CreateOrder dto, String userEmail) {

		Orders order = new Orders();
		order.setStatus(OrderStatus.CREATED);
		order.setCreatedAt(LocalDateTime.now());
		order.setPickupTime(dto.getPickupTime());
		order.setEmail(dto.getEmail());

		if (dto.getEmail() == null) {
			throw new RuntimeException("email is empty");
		}
		if (dto.getPickupTime().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("pickup time cannot be in the past");
		}

		if (userEmail != null) {
			User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
			order.setUser(user);
		}

		for (OrderItemRequest itemDto : dto.getItems()) {

			Pizza pizza = pizzaRepository.findById(itemDto.getPizzaId())
					.orElseThrow(() -> new RuntimeException("Pizza not found"));

			if (!pizza.isAvailable()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Pizza '" + pizza.getName() + "' is currently unavailable");
			}

			BigDecimal unitPrice = pizza.getBasePrice();

			OrderItem item = new OrderItem(order, pizza, itemDto.getSize(), itemDto.getQuantity(), unitPrice);

			order.addOrderItem(item);
		}

		order.recalculateTotal();

		Orders saved = orderRepository.save(order);

		sendEmailAndUpdateStatus(saved);

		List<OrderItemResponse> itemDtos = saved
				.getOrderItems().stream().map(item -> new OrderItemResponse(item.getPizza().getId(),
						item.getPizza().getName(), item.getSize().name(), item.getQuantity(), item.getPrice()))
				.toList();

		return new OrderResponse(saved.getPublicId(), saved.getEmail(), saved.getTotalPrice(), saved.getStatus(),
				saved.getCreatedAt(), saved.getPickupTime(), itemDtos);
	}

	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll().stream().map(this::toResponseDto).toList();
	}

	@Transactional
	public OrderResponse closeOrder(String orderId) {

		Orders order = orderRepository.findByPublicId(orderId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		if (order.getStatus() != OrderStatus.PREPARING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PREPARING orders can be closed");
		}

		order.setStatus(OrderStatus.CLOSED);

		Orders saved = orderRepository.save(order);

		return toResponseDto(saved);
	}

	@Transactional
	private void sendEmailAndUpdateStatus(Orders order) {
		try {
			order.setStatus(OrderStatus.PREPARING);
			orderRepository.save(order);
			emailService.sendOrderConfirmation(order);

		} catch (Exception e) {

			// order stays CREATED
			System.err.println("Email failed for order " + order.getId());
			e.printStackTrace();
		}
	}

	private OrderResponse toResponseDto(Orders order) {

		List<OrderItemResponse> items = order
				.getOrderItems().stream().map(item -> new OrderItemResponse(item.getPizza().getId(),
						item.getPizza().getName(), item.getSize().name(), item.getQuantity(), item.getPrice()))
				.toList();
		OrderResponse dto = new OrderResponse(order.getPublicId(), order.getEmail(), order.getTotalPrice(),
				order.getStatus(), order.getCreatedAt(), order.getPickupTime(), items);

		return dto;
	}

	public List<OrderSummaryDto> getOrderHistory(Long userId) {
		return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(order -> {

			List<OrderItemSummaryDto> itemDtos = order.getOrderItems().stream()
					.map(item -> new OrderItemSummaryDto(item.getPizza().getName(), item.getSize().name(),
							item.getQuantity(), item.getPrice()))
					.toList();

			return new OrderSummaryDto(order.getId(), order.getCreatedAt(), order.getTotalPrice(), order.getStatus(),
					itemDtos);
		}).toList();
	}

	public OrderResponse getOrder(String publicId, User user) {

		Orders order = orderRepository.findByPublicIdAndUserId(publicId, user.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		return toResponseDto(order);
	}

}
