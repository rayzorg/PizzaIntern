package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import com.example.internproject.dto.CreateOrder;
import com.example.internproject.dto.OrderItemRequest;
import com.example.internproject.dto.OrderResponse;
import com.example.internproject.models.OrderItem;
import com.example.internproject.models.OrderStatus;
import com.example.internproject.models.Orders;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Role;
import com.example.internproject.models.Size;
import com.example.internproject.models.Topping;
import com.example.internproject.models.User;
import com.example.internproject.repository.OrderRepository;
import com.example.internproject.repository.PizzaRepository;
import com.example.internproject.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {
	
	@Autowired
    private OrderService orderService;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ToppingService toppingService;
    
    @Autowired 
    private UserService userService;
    @MockitoBean
    private EmailService emailService;
	
	@Test
	void shouldCreateOrder() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);

		    CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));

		    OrderResponse response = orderService.placeOrder(dto, dto.getEmail());

		    
		    Orders saved = orderRepository.findById(response.getOrderId()).orElseThrow();

		    assertThat(saved.getOrderItems()).hasSize(1);
		    assertThat(saved.getStatus()).isEqualTo(OrderStatus.PREPARING);
	}
	@Test
	void shouldCalculatePriceTotal() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);
         
         CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));
		    
		    OrderResponse response = orderService.placeOrder(dto,dto.getEmail());

		    assertThat(response.getTotalPrice())
		        .isEqualByComparingTo(BigDecimal.valueOf(20));
	}
	@Test
	void shouldHaveUserAssociatedWithOrder() {
		
		String name = "John Doe";
        String email = "john@test.com";
        String password = "secret123";
        String phone = "123456789";
        Role role = Role.CUSTOMER;
        
        User user = userService.register(name, email, password, phone, role);
        
    	Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);
         
         CreateOrder dto = new CreateOrder();
		    dto.setEmail(user.getEmail());
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));

		    OrderResponse response = orderService.placeOrder(dto,dto.getEmail());

		    Orders order = orderRepository.findById(response.getOrderId()).orElseThrow();
		    assertThat(order.getUser()).isNotNull();
		    assertThat(order.getUser().getEmail()).isEqualTo(user.getEmail());
         
	}
	

	@Test
	void shouldNotCreateOrderWithoutEmail() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);
         
         CreateOrder dto = new CreateOrder();
		   //no email dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));
		    
		    
		    assertThatThrownBy(() ->{ orderService.placeOrder(dto, dto.getEmail());})
	        .isInstanceOf(Exception.class)
	        .hasMessageContaining("email");	
		    }
	@Test
	void orderShouldNotBeInThePast() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);
         
         CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    //in the past
		    dto.setPickupTime(LocalDateTime.now().minusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));
		    
		    assertThatThrownBy(() -> orderService.placeOrder(dto,dto.getEmail()))
	        .isInstanceOf(IllegalArgumentException.class)
	        .hasMessageContaining("pickup");
		    
	}
	@Test
	void orderShouldGoToPreparingAndEmailSentWhenPlaced() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza small",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);

		    CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));

		    OrderResponse response = orderService.placeOrder(dto, dto.getEmail());
		    
		    Orders order = orderRepository.findById(response.getOrderId()).orElseThrow();
		    assertThat(order.getStatus()).isEqualTo(OrderStatus.PREPARING);

		    verify(emailService, times(1))
		        .sendOrderConfirmation(any());
	}
	@Test
	void orderItemShouldHaveRightCalculationOfPizzas() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaRepository.save(pepperoniPizza);

		    CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));

		    OrderResponse response = orderService.placeOrder(dto, dto.getEmail());
		    
		    Orders order = orderRepository.findById(response.getOrderId()).orElseThrow();

		    OrderItem item = order.getOrderItems().get(0);
		    assertThat(item.getSubTotal())
		        .isEqualByComparingTo(BigDecimal.valueOf(20));
	}
	
	@Test
	void shouldNotCreateOrderIfPizzaIsNotAvailable() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pepperoniPizza.setAvailable(false);//pizza not available
         pizzaRepository.save(pepperoniPizza);
         
         CreateOrder dto = new CreateOrder();
		    dto.setEmail("test@test.com");
		    dto.setPickupTime(LocalDateTime.now().plusHours(1));
		    
		    OrderItemRequest orderItem=new OrderItemRequest();
		    orderItem.setPizzaId(pepperoniPizza.getId());
		    orderItem.setQuantity(2);
		    orderItem.setSize(Size.MEDIUM);
		    
		    dto.setItems(List.of(orderItem));
         
         assertThatThrownBy(() -> orderService.placeOrder(dto,dto.getEmail()))
	        .isInstanceOf(ResponseStatusException.class)
	        .hasMessageContaining("unavailable");
	}


}
