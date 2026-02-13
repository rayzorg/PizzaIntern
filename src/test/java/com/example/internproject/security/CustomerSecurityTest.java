package com.example.internproject.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import com.example.internproject.dto.OrderItemRequestDto;
import com.example.internproject.dto.OrderItemResponseDto;
import com.example.internproject.dto.OrderPlacedResponseDto;
import com.example.internproject.dto.PlaceOrderDto;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Role;
import com.example.internproject.models.Size;
import com.example.internproject.models.User;
import com.example.internproject.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class CustomerSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
	    objectMapper.findAndRegisterModules(); // this registers JavaTimeModule automatically
	}
	
	@Test
	void givenCustomer_WhenAccessingMyOrdersCustomers_ThenOrdersShow() throws Exception {
		User customer = new User();
		customer.setEmail("customer@test.com");
		customer.setPassword("password");
		customer.setRole(Role.CUSTOMER);

		UserPrincipal principal = new UserPrincipal(customer);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null,
				principal.getAuthorities());

		mockMvc.perform(get("/api/orders/myorders").with(authentication(auth))).andExpect(status().isOk());
	}

	@Test
	void givenAdmin_WhenAccessingMyOrdersCustomers_ThenAccessDenied() throws Exception {
		mockMvc.perform(get("/api/orders/myorders").with(user("admin").roles("ADMIN")))
				.andExpect(status().isForbidden());
	}

	@Test
	void givenGuest_WhenAccessingMyOrdersCustomers_ThenAccessDenied() throws Exception {
		mockMvc.perform(get("/api/orders/myorders").with(anonymous())).andExpect(status().isUnauthorized());

	}

	@Test
	void givenCustomer_WhenPlaceOrder_ShouldCreateOrder() throws Exception {
		User customer = new User();
		customer.setEmail("customer@test.com");
		customer.setPassword("password");
		customer.setRole(Role.CUSTOMER);
		
		
        PlaceOrderDto dto = new PlaceOrderDto();
        dto.setPickupTime(LocalDateTime.now().plusHours(1));
        dto.setEmail(customer.getEmail());
        
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setPizzaId(1L);
        item.setQuantity(2);
        item.setSize(Size.MEDIUM);
        dto.setItems(List.of(item));

        
		UserPrincipal principal = new UserPrincipal(customer);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null,
				principal.getAuthorities());
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)).with(authentication(auth)))
                .andExpect(status().isOk());

        verify(orderService, times(1)).placeOrder(any(PlaceOrderDto.class), anyString());
    }
	

	@Test
	void givenAdmin_WhenPlaceOrder_ThenAccessDenied() throws Exception {
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setEmail("test@test.com");
		
		OrderItemRequestDto items=new OrderItemRequestDto();
		items.setPizzaId(1L);
		items.setQuantity(1);
		items.setSize(Size.MEDIUM);
		
		dto.setItems(List.of(items));
		dto.setPickupTime(LocalDateTime.now().plusHours(1));

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)).with(user("admin").roles("ADMIN")))
				.andExpect(status().isForbidden()); 

		verify(orderService, never()).placeOrder(any(), anyString());

	}

	@Test
	void givenGuest_WhenPlaceOrder_ThenAccessDenied() throws Exception {
		PlaceOrderDto dto = new PlaceOrderDto();
		dto.setPickupTime(LocalDateTime.now().plusHours(1));

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)).with(anonymous())).andExpect(status().isUnauthorized()); 

		verify(orderService, never()).placeOrder(any(), anyString());
	}

	@Test
	void givenCustomer_whenGetOrderByPublicId_thenSuccess() throws Exception {

		User customer = new User();
		customer.setEmail("customer@test.com");

		OrderItemResponseDto item = new OrderItemResponseDto(1L, "margharita", "LARGE", 2, BigDecimal.ONE);

		when(orderService.getOrderWhenPlaced(eq("public123"), any()))
				.thenReturn(new OrderPlacedResponseDto("public123", "customer@test.com", BigDecimal.TEN,
						OrderStatus.PREPARING, LocalDateTime.now(), LocalDateTime.now().plusHours(1), List.of(item)));

		mockMvc.perform(get("/api/orders/public123").with(user("customer@test.com").roles("CUSTOMER")))
				.andExpect(status().isOk());
	}

	@Test
	void givenAdmin_whenGetOrderByPublicId_thenForbidden() throws Exception {

		mockMvc.perform(get("/api/orders/public123").with(user("admin").roles("ADMIN")))
				.andExpect(status().isForbidden());
	}

	@Test
	void givenGuest_whenGetOrderByPublicId_thenUnauthorized() throws Exception {

		mockMvc.perform(get("/api/orders/public123")).andExpect(status().isUnauthorized());
	}
}