package com.ey.service;

import java.util.List;

import com.ey.dto.request.CreateExpenseRequest;
import com.ey.dto.request.UpdateExpenseRequest;
import com.ey.dto.response.ExpenseMonthResponse;
import com.ey.dto.response.ExpenseResponse;

public interface ExpenseService {
	ExpenseResponse createExpense(CreateExpenseRequest request);

    List<ExpenseResponse> getAllExpenses();

    ExpenseMonthResponse getExpensesByMonth(String month);

    ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request);

    void disableExpense(Long id);

}
