package com.ey.dto.response;

import java.util.List;

public class ExpenseMonthResponse {

    private String month;
    private double totalAmount;
    private List<ExpenseResponse> expenses;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<ExpenseResponse> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<ExpenseResponse> expenses) {
		this.expenses = expenses;
	}

    
}

