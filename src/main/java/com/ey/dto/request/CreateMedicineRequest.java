package com.ey.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateMedicineRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String batchNumber;

    @Future
    private LocalDate expiryDate;

    @Positive
    private double price;

    @PositiveOrZero
    private int quantity;

    @NotNull
    private Long dealerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

    // getters & setters
}
