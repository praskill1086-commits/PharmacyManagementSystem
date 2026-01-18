package com.ey.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RefillResponse {

    private Long id;
    private String customerName;
    private String medicineName;
    private int quantity;
    private int intervalDays;
    private LocalDate nextRefillDate;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
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
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

    
}
