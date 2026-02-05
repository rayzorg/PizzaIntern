package com.example.internproject.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Pizza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	private String description;
	@Column(nullable = false)
	private BigDecimal price;
	@Column(nullable = false)
	private boolean available;
	@Column(nullable = false)
	private String imageUrl;

	@ManyToMany
	@JoinTable(name = "pizza_toppings", joinColumns = @JoinColumn(name = "pizza_id"), inverseJoinColumns = @JoinColumn(name = "topping_id"))
	private Set<Topping> toppings = new HashSet<>();;

	public Pizza() {
		// JPA
	}

	public Pizza(String name, String description, BigDecimal price, String imageUrl) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.available = true;
	}

	public Long getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getBasePrice() {
		return price;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Set<Topping> getToppings() {
		return toppings;

	}

	public void setToppings(Set<Topping> toppings) {
		this.toppings = toppings;
	}

}
