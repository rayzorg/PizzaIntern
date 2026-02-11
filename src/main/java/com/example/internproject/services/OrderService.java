package com.example.internproject.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.internproject.dto.AdminViewOrdersDto;
import com.example.internproject.dto.PlaceOrderDto;
import com.example.internproject.dto.OrderItemRequestDto;
import com.example.internproject.dto.OrderItemResponseDto;
import com.example.internproject.dto.OrderItemSummaryDto;
import com.example.internproject.dto.OrderPlacedResponseDto;
import com.example.internproject.dto.OrderSummaryCustomerDto;
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
@Transactional
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
	public OrderPlacedResponseDto placeOrder(PlaceOrderDto dto, String userEmail) {

		if (userEmail == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in to place an order");
		}
		Orders order = new Orders();
		order.setStatus(OrderStatus.CREATED);
		order.setCreatedAt(LocalDateTime.now());
		order.setPickupTime(dto.getPickupTime());
		order.setEmail(userEmail);

		if (dto.getPickupTime().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("pickup time cannot be in the past");
		}

		if (userEmail != null) {
			User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
			order.setUser(user);
		}

		for (OrderItemRequestDto itemDto : dto.getItems()) {

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

		List<OrderItemResponseDto> itemDtos = saved
				.getOrderItems().stream().map(item -> new OrderItemResponseDto(item.getPizza().getId(),
						item.getPizza().getName(), item.getSize().name(), item.getQuantity(), item.getPrice()))
				.toList();

		return new OrderPlacedResponseDto(saved.getPublicId(), saved.getEmail(), saved.getTotalPrice(),
				saved.getStatus(), saved.getCreatedAt(), saved.getPickupTime(), itemDtos);
	}

	// admin
	public List<AdminViewOrdersDto> getAllOrdersForAdmin() {
		return orderRepository.findAll().stream().map(this::toAdminViewDto).toList();
	}

	private AdminViewOrdersDto toAdminViewDto(Orders order) {

		List<OrderItemResponseDto> items = order
				.getOrderItems().stream().map(item -> new OrderItemResponseDto(item.getPizza().getId(),
						item.getPizza().getName(), item.getSize().name(), item.getQuantity(), item.getPrice()))
				.toList();

		return new AdminViewOrdersDto(order.getId(), order.getEmail(), order.getTotalPrice(), order.getStatus(),
				order.getCreatedAt(), order.getPickupTime(), items);
	}

	@Transactional
	public OrderPlacedResponseDto closeOrder(Long orderId) {

		Orders order = orderRepository.findById(orderId)
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
			e.printStackTrace();
		}
	}

	private OrderPlacedResponseDto toResponseDto(Orders order) {

		List<OrderItemResponseDto> items = order
				.getOrderItems().stream().map(item -> new OrderItemResponseDto(item.getPizza().getId(),
						item.getPizza().getName(), item.getSize().name(), item.getQuantity(), item.getPrice()))
				.toList();
		OrderPlacedResponseDto dto = new OrderPlacedResponseDto(order.getPublicId(), order.getEmail(),
				order.getTotalPrice(), order.getStatus(), order.getCreatedAt(), order.getPickupTime(), items);

		return dto;
	}

	// customer
	public List<OrderSummaryCustomerDto> getOrderHistoryForCustomer(Long userId) {
		return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(order -> {

			List<OrderItemSummaryDto> itemDtos = order.getOrderItems().stream()
					.map(item -> new OrderItemSummaryDto(item.getPizza().getName(), item.getSize().name(),
							item.getQuantity(), item.getPrice()))
					.toList();

			return new OrderSummaryCustomerDto(order.getPublicId(), order.getCreatedAt(), order.getTotalPrice(),
					order.getStatus(), itemDtos);
		}).toList();
	}

	public OrderPlacedResponseDto getOrderWhenPlaced(String publicId, User user) {

		Orders order = orderRepository.findByPublicIdAndUserId(publicId, user.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		return toResponseDto(order);
	}

}
