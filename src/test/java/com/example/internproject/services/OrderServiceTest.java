package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.internproject.dto.OrderItemRequestDto;
import com.example.internproject.dto.OrderPlacedResponseDto;
import com.example.internproject.dto.PlaceOrderDto;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Orders;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Size;
import com.example.internproject.models.User;
import com.example.internproject.repository.OrderRepository;
import com.example.internproject.repository.PizzaRepository;
import com.example.internproject.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	PizzaRepository pizzaRepository;

	@Mock
	EmailService emailService;

	@InjectMocks
	OrderService orderService;

	@Test
	void givenValidOrder_whenPlaceOrder_thenOrderPlaced() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setPickupTime(LocalDateTime.now().plusHours(1));

		OrderItemRequestDto orderItem = new OrderItemRequestDto();
		orderItem.setPizzaId(1L);
		orderItem.setQuantity(2);
		orderItem.setSize(Size.MEDIUM);

		dto.setItems(List.of(orderItem));

		Pizza pizza = new Pizza("hawai", "test", new BigDecimal("10.00"), "test");

		User user = new User();
		user.setEmail("user@test.com");

		when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
		when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
		when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// WHEN
		orderService.placeOrder(dto, user.getEmail());

		// THEN
		verify(orderRepository, atLeastOnce()).save(any(Orders.class));
	}

	@Test
	void givenInvalidOrder_whenPlaceOrder_WithNoEmail_OrderNotPlaced() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();

		// WHEN THEN
		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.placeOrder(dto, null));

		assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
		assertTrue(exception.getReason().contains("logged in"));

		verify(orderRepository, never()).save(any());
	}

	@Test
	void givenInvalidOrder_whenPlaceOrder_WithPickupTimeInPast_OrderNotPlaced() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setPickupTime(LocalDateTime.now().minusHours(1));

		// WHEN THEN
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.placeOrder(dto, "test@test.com");
		});

		verify(orderRepository, never()).save(any());

	}

	@Test
	void givenValidOrder_whenPlaceOrder_WithTotalEqualTo20__OrderPlaced() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setPickupTime(LocalDateTime.now().plusHours(1));

		OrderItemRequestDto item = new OrderItemRequestDto();
		item.setPizzaId(1L);
		item.setQuantity(2);
		item.setSize(Size.MEDIUM);

		dto.setItems(List.of(item));

		String userEmail = "user@test.com";

		User user = new User();
		user.setEmail(userEmail);

		Pizza pizza = new Pizza("Margherita", "Classic pizza", new BigDecimal("10.00"), // price per unit
				"img.png");

		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
		when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
		when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// WHEN
		OrderPlacedResponseDto response = orderService.placeOrder(dto, userEmail);

		// THEN
		assertNotNull(response);
		assertEquals(new BigDecimal("20.00"), response.getTotalPrice());
	}

	@Test
	void givenInvalidOrder_whenPlaceOrder_WithUnavailablePizza_OrderNotPlaced() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setPickupTime(LocalDateTime.now().plusHours(1));

		OrderItemRequestDto item = new OrderItemRequestDto();
		item.setPizzaId(1L);
		item.setQuantity(1);
		item.setSize(Size.MEDIUM);

		dto.setItems(List.of(item));

		String userEmail = "user@test.com";

		User user = new User();
		user.setEmail(userEmail);

		Pizza pizza = new Pizza("Pepperoni", "Spicy", new BigDecimal("12.00"), "img.png");
		pizza.setAvailable(false);

		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
		when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));

		// WHEN + THEN
		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.placeOrder(dto, userEmail));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertTrue(exception.getReason().contains("unavailable"));
		
		verify(orderRepository, never()).save(any());

	}
}
