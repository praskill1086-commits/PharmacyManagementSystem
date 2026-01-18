package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateExpenseRequest;
import com.ey.dto.request.UpdateExpenseRequest;
import com.ey.dto.response.ExpenseMonthResponse;
import com.ey.dto.response.ExpenseResponse;
import com.ey.entity.Expense;
import com.ey.repository.ExpenseRepository;
import com.ey.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setActive(true);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        return mapToResponse(expenseRepo.save(expense));
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepo.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseMonthResponse getExpensesByMonth(String month) {
        YearMonth ym = YearMonth.parse(month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Expense> expenses =
                expenseRepo.findByActiveTrueAndDateBetween(start, end);

        double total = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        ExpenseMonthResponse resp = new ExpenseMonthResponse();
        resp.setMonth(month);
        resp.setTotalAmount(total);
        resp.setExpenses(
                expenses.stream().map(this::mapToResponse).toList()
        );

        return resp;
    }

    @Override
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {
        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        return mapToResponse(expenseRepo.save(expense));
    }

    @Override
    public void disableExpense(Long id) {
        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setActive(false);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        expenseRepo.save(expense);
    }

    private ExpenseResponse mapToResponse(Expense e) {
        ExpenseResponse r = new ExpenseResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setAmount(e.getAmount());
        r.setDate(e.getDate());
        r.setCategory(e.getCategory());
        r.setActive(e.isActive());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setUpdatedBy(
            e.getUpdatedBy() != null ? e.getUpdatedBy().getUsername() : null
        );
        return r;
    }
}
