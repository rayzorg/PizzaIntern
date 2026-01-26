package com.example.internproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.internproject.models.Pizza;
import com.example.internproject.repository.PizzaRepository;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {

	@InjectMocks
	private PizzaService pizzaService;

	@Mock
	private PizzaRepository pizzaRepository;

	@Test
	void shouldShowPizzasWhenGoingToPizzaMenu() {
		// given
		Pizza pizza = new Pizza("hawai", "test", new BigDecimal("10.00"), "test");
		// when
		when(pizzaRepository.findAll()).thenReturn(List.of(pizza));
		// then
		assertThat(pizzaService.getAll()).isNotEmpty();
	}

	@Test
	void pizzaShouldBeUnavailableWhenAdminDisablesPizzaInAdminpanel() {
		// given
		Pizza pizza = new Pizza("hawai", "test", new BigDecimal("10.00"), "test");
		// when
		when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
		pizzaService.updateAvailability(1L, false);
		// then
		assertThat(pizza.isAvailable()).isFalse();
	}

	@Test
	void pizzaShouldHaveCorrectPriceAtCreation() {
		// given
		Pizza pizza = new Pizza("hawai", "test", new BigDecimal("10.00"), "test");
		// then
		assertThat(pizza.getBasePrice()).isEqualByComparingTo(new BigDecimal("10.00"));
	}

}
