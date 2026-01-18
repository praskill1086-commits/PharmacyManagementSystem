package com.ey.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateOfferRequest {

    @NotBlank
    private String name;

    @Positive
    @Max(100)
    private int discountPercent;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

    // getters & setters
}
