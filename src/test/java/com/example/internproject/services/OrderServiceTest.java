package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.example.internproject.dto.OrderItemRequestDto;
import com.example.internproject.dto.PlaceOrderDto;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Orders;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Size;
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
	void givenValidOrder_whenPlaceOrder_thenEmailIsSent() {
		// GIVEN
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setEmail("test@test.com");
		dto.setPickupTime(LocalDateTime.now().plusHours(1));
		
		
		OrderItemRequestDto orderItem = new OrderItemRequestDto();
		orderItem.setPizzaId(1L);
		orderItem.setQuantity(2);
		orderItem.setSize(Size.MEDIUM);

		dto.setItems(List.of(orderItem));

		Pizza pizza = new Pizza("hawai", "test", new BigDecimal("10.00"), "test");

		when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
		when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// WHEN
		orderService.placeOrder(dto, null);

		// THEN
		verify(emailService).sendOrderConfirmation(any(Orders.class));
	}
}
