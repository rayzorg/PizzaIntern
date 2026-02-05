package com.example.internproject.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.internproject.dto.ToppingsResponseDto;
import com.example.internproject.services.ToppingService;

@RestController
@RequestMapping("/api/toppings")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class ToppingController {

	private final ToppingService toppingService;

	public ToppingController(ToppingService toppingService) {
		this.toppingService = toppingService;
	}

	@GetMapping
	public List<ToppingsResponseDto> getAllToppings() {
		return toppingService.getAll().stream().map(t -> new ToppingsResponseDto(t.getId(), t.getName())).toList();
	}
}
