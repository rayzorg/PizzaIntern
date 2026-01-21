package com.example.internproject.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;



@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;  // menu pizza

    @Enumerated(EnumType.STRING)
    private Size size;

    private int quantity;

    private BigDecimal price; // snapshot price for this item
    
    
    public OrderItem() {
        // JPA
    }
    
    public OrderItem(Orders order,Pizza pizza, Size size, int quantity, BigDecimal price) {
       this.order=order;
    	this.pizza = pizza;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }
    
    public void setOrder(Orders order) {
        this.order = order;
    }

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }


	public Long getId() {
		return id;
	}

	public Orders getOrder() {
		return order;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public Size getSize() {
		return size;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}
    
}
