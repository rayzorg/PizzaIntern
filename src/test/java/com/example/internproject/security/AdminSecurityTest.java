package com.example.internproject.security;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.internproject.dto.AdminViewOrdersDto;
import com.example.internproject.dto.OrderPlacedResponseDto;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.services.OrderService;
import com.example.internproject.services.PizzaService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class AdminSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	@MockitoBean
	private PizzaService pizzaService;

	@Test
	void givenAdmin_whenGetAllOrders_thenReturnsOrders() throws Exception {

		UserRequestPostProcessor adminUser = user("admin").roles("ADMIN");

		AdminViewOrdersDto orderDto = new AdminViewOrdersDto(1L, "test@test.com", new BigDecimal("20.00"),
				OrderStatus.PREPARING, LocalDateTime.now(), LocalDateTime.now().plusHours(1), List.of());

		when(orderService.getAllOrdersForAdmin()).thenReturn(List.of(orderDto));

		mockMvc.perform(get("/api/admin/orders").with(adminUser)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].orderId").value(1)).andExpect(jsonPath("$[0].email").value("test@test.com"));

		verify(orderService, times(1)).getAllOrdersForAdmin();
	}

	@Test
	void givenCustomer_whenGetAllOrdersforAdmin_thenAccessDenied() throws Exception {

		UserRequestPostProcessor customer = user("customer").roles("CUSTOMER");

		mockMvc.perform(get("/api/admin/orders").with(customer)).andExpect(status().isForbidden());

		verify(orderService, never()).getAllOrdersForAdmin();
	}

	@Test
	void givenGuestUser_whenGetAllOrdersforAdmin_thenAccessDenied() throws Exception {
		mockMvc.perform(get("/api/admin/orders").with(anonymous())).andExpect(status().isUnauthorized());

		verify(orderService, never()).getAllOrdersForAdmin();
	}

	@Test
	void givenAdmin_whenCloseOrder_thenOrderClosed() throws Exception {
		Long orderId = 1L;

		UserRequestPostProcessor adminUser = user("admin").roles("ADMIN");

		OrderPlacedResponseDto closedOrder = new OrderPlacedResponseDto("uuid-123", "test@test.com",
				new BigDecimal("20.00"), OrderStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
				List.of());

		when(orderService.closeOrder(orderId)).thenReturn(closedOrder);

		mockMvc.perform(put("/api/admin/orders/{orderId}/close", orderId).with(adminUser)).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CLOSED"));

		verify(orderService, times(1)).closeOrder(orderId);
	}

	@Test
	void givenCustomer_whenCloseOrder_thenAccessDenied() throws Exception {
		Long orderId = 1L;

		UserRequestPostProcessor customer = user("customer").roles("CUSTOMER");

		OrderPlacedResponseDto closedOrder = new OrderPlacedResponseDto("uuid-123", "test@test.com",
				new BigDecimal("20.00"), OrderStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
				List.of());

		when(orderService.closeOrder(orderId)).thenReturn(closedOrder);

		mockMvc.perform(put("/api/admin/orders/{orderId}/close", orderId).with(customer))
				.andExpect(status().isForbidden());

		verify(orderService, never()).closeOrder(orderId);
	}

	@Test
	void givenGuest_whenCloseOrder_thenAccessDenied() throws Exception {
		Long orderId = 1L;

		OrderPlacedResponseDto closedOrder = new OrderPlacedResponseDto("uuid-123", "test@test.com",
				new BigDecimal("20.00"), OrderStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
				List.of());

		when(orderService.closeOrder(orderId)).thenReturn(closedOrder);

		mockMvc.perform(put("/api/admin/orders/{orderId}/close", orderId).with(anonymous()))
				.andExpect(status().isUnauthorized());

		verify(orderService, never()).closeOrder(orderId);
	}

	@Test
	void givenAdmin_whenUpdateAvailability_thenSuccess() throws Exception {
		UserRequestPostProcessor admin = user("admin").roles("ADMIN");

		mockMvc.perform(put("/api/admin/pizzas/{id}/availability", 1).param("available", "true").with(admin))
				.andExpect(status().isOk());

		verify(pizzaService, times(1)).updateAvailability(1L, true);
	}

	@Test
	void givenCustomer_whenUpdateAvailability_thenAccessDenied() throws Exception {
		UserRequestPostProcessor customer = user("customer").roles("CUSTOMER");

		mockMvc.perform(put("/api/admin/pizzas/{id}/availability", 1).param("available", "false").with(customer))
				.andExpect(status().isForbidden());

		verify(pizzaService, never()).updateAvailability(anyLong(), anyBoolean());
	}

	@Test
	void givenGuest_whenUpdateAvailability_thenAccessDenied() throws Exception {
		mockMvc.perform(put("/api/admin/pizzas/{id}/availability", 1).param("available", "true").with(anonymous()))
				.andExpect(status().isUnauthorized());

		verify(pizzaService, never()).updateAvailability(anyLong(), anyBoolean());
	}

}
