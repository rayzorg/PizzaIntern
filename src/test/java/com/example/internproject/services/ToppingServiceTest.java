package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.internproject.dto.PizzaResponseDto;
import com.example.internproject.models.Topping;
import com.example.internproject.repository.ToppingRepository;

import jakarta.transaction.Transactional;
@SpringBootTest
@Transactional
public class ToppingServiceTest {
	
	@Autowired
	private ToppingService toppingService;
	@Autowired
	private ToppingRepository toppingRepository;
	
	@Test
	void ToppingShouldBeCreated() {
		 Topping cheese = toppingService.addTopping(new Topping("Cheese"));         
         
         assertThat(toppingRepository.findById(cheese.getId())).isPresent();
         
	}
	
	@Test
	void shouldRetrieveAllToppings() {
		 toppingService.addTopping(new Topping("Cheese"));
         toppingService.addTopping(new Topping("Pepperoni"));
         
         
         List<Topping> toppings = toppingService.getAll();

         assertThat(toppings).isNotEmpty();
         
         Topping topping1=toppings.get(0);
         assertThat(topping1.getName()).isEqualTo("Cheese");
         }

}
