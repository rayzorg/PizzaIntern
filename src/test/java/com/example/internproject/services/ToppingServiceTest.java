package com.example.internproject.services;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.internproject.models.Topping;
import com.example.internproject.repository.ToppingRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ToppingServiceTest {
	@InjectMocks
	private ToppingService toppingService;

	@Mock
	private ToppingRepository toppingRepository;

	@Test
	void ToppingShouldHaveCorrectNameAtCreation() {
		// given
		Topping cheese = new Topping("cheese");
		// then
		assertThat(cheese.getName()).isEqualTo("cheese");
	}

}
