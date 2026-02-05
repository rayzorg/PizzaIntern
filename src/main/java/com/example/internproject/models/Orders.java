package com.example.internproject.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, updatable = false, length = 36)
	private String publicId;

	private BigDecimal totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private LocalDateTime createdAt;
	@Column(nullable = false)
	private LocalDateTime pickupTime;

	@Column(nullable = false)
	private String email;

	@ManyToOne(optional = true)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	public Orders() {
		// JPA
	}

	public Orders(User user) {
		this.user = user;
		this.status = OrderStatus.CREATED;
		this.createdAt = LocalDateTime.now();
		this.totalPrice = BigDecimal.ZERO;
	}

	@PrePersist
	public void initPublicId() {
		if (this.publicId == null) {
			this.publicId = UUID.randomUUID().toString();
		}
	}

	public Long getId() {
		return id;
	}

	public String getPublicId() {
		return publicId;
	}

	public User getUser() {
		return user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void addOrderItem(OrderItem item) {
		orderItems.add(item);
		item.setOrder(this);
		recalculateTotal();
	}

	public void removeOrderItem(OrderItem item) {
		orderItems.remove(item);
		item.setOrder(null);
		recalculateTotal();
	}

	public void recalculateTotal() {
		this.totalPrice = orderItems.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Orders))
			return false;
		Orders other = (Orders) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
