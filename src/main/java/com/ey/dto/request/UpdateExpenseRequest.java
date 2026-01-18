package com.ey.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.ey.enums.ExpenseCategory;

public class UpdateExpenseRequest {

    @NotBlank
    private String title;

    @Positive
    private double amount;

    @NotNull
    private LocalDate date;

    @NotNull
    private ExpenseCategory category;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public ExpenseCategory getCategory() {
		return category;
	}

	public void setCategory(ExpenseCategory category) {
		this.category = category;
	}

    
}
