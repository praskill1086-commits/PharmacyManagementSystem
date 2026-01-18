package com.ey.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RefillPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Medicine medicine;

    @Positive
    private int quantity;

    @Positive
    private int intervalDays;

    private LocalDate nextRefillDate;

    private boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    private User updatedBy;

    public RefillPlan() {}

    public RefillPlan(Customer customer, Medicine medicine, int quantity, int intervalDays, LocalDate nextRefillDate) {
        this.customer = customer;
        this.medicine = medicine;
        this.quantity = quantity;
        this.intervalDays = intervalDays;
        this.nextRefillDate = nextRefillDate;
        this.active = true;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(int intervalDays) {
		this.intervalDays = intervalDays;
	}

	public LocalDate getNextRefillDate() {
		return nextRefillDate;
	}

	public void setNextRefillDate(LocalDate nextRefillDate) {
		this.nextRefillDate = nextRefillDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}
    
    
}
