package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.internproject.dto.PizzaResponseDto;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Topping;
import com.example.internproject.repository.PizzaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class PizzaServiceTest {

	 @Autowired
	 private PizzaService pizzaService;

	 @Autowired
	 private PizzaRepository pizzaRepository;
	 
	 @Autowired
	 private ToppingService toppingService;
	 
	@Test
	void shouldRetrievePizzas() {
		
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
        Topping ham = toppingService.addTopping(new Topping("Ham"));
        Topping pineapple = toppingService.addTopping(new Topping("Pineapple"));
        
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaService.addPizza(pepperoniPizza);

         Pizza hawaiian = new Pizza(
                 "Hawaiian",
                 "Ham and pineapple",
                 new BigDecimal("9.50"),
                 "pizzas/hawai.jpg"
         );
         hawaiian.setToppings(Set.of(cheese, ham, pineapple));
         pizzaService.addPizza(hawaiian);
         
         
         List<PizzaResponseDto> pizzas = pizzaService.getAll();

         assertThat(pizzas).isNotEmpty();

         PizzaResponseDto dto = pizzas.get(0);
         assertThat(dto.getName()).isEqualTo("Margherita");
         assertThat(dto.isAvailable()).isTrue();
	}
	
	@Test
	void pizzaShouldBeUnavailable() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaService.addPizza(pepperoniPizza);
         
         
	    pizzaService.updateAvailability(pepperoniPizza.getId(), false);

	    Pizza updatedPizza = pizzaRepository
	        .findById(pepperoniPizza.getId())
	        .orElseThrow();

	    assertThat(updatedPizza.isAvailable()).isFalse();
		
	}
	
	@Test
	void shouldCreatePizza() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping ham = toppingService.addTopping(new Topping("Ham"));
        Topping pineapple = toppingService.addTopping(new Topping("Pineapple"));
        
		Pizza hawaiian = new Pizza(
                "Hawaiian",
                "Ham and pineapple",
                new BigDecimal("9.50"),
                "pizzas/hawai.jpg"
        );
        hawaiian.setToppings(Set.of(cheese, ham, pineapple));
        pizzaService.addPizza(hawaiian);
		
		 assertThat(hawaiian.getId()).isNotNull();
		 assertThat(hawaiian.getName()).isEqualTo("Hawaiian");
		 assertThat(hawaiian.isAvailable()).isTrue();

		 assertThat(pizzaRepository.findById(hawaiian.getId())).isPresent();
		
	}
	
	@Test
	void ShouldHaveToppings() {
		Topping cheese = toppingService.addTopping(new Topping("Cheese"));
        Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
      
		 Pizza pepperoniPizza = new Pizza(
                 "Pepperoni",
                 "Spicy pepperoni pizza",
                 new BigDecimal("10.00"),
                 "pizzas/pepperoni.jpg"
         );
         pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
         pizzaService.addPizza(pepperoniPizza);
         
         List<PizzaResponseDto> pizzas = pizzaService.getAll();

         PizzaResponseDto dto = pizzas.get(0);

         assertThat(dto.getToppings())
             .containsExactlyInAnyOrder("Cheese");
	}
}
