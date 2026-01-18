package com.ey.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class UpdateRefillRequest {

    @Positive
    private int quantity;

    @Positive
    private int intervalDays;

    @NotNull
    private LocalDate nextRefillDate;

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

   
}

