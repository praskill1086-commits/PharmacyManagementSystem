package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateExpenseRequest;
import com.ey.dto.request.UpdateExpenseRequest;
import com.ey.dto.response.ExpenseMonthResponse;
import com.ey.dto.response.ExpenseResponse;
import com.ey.entity.Expense;
import com.ey.exception.ExpenseNotFoundException;
import com.ey.repository.ExpenseRepository;
import com.ey.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseServiceImpl.class);

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request) {

        logger.info("Creating expense with title={}", request.getTitle());

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setActive(true);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        Expense saved = expenseRepo.save(expense);

        logger.info("Expense created successfully, id={}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {

        logger.info("Fetching all active expenses");

        return expenseRepo.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseMonthResponse getExpensesByMonth(String month) {

        logger.info("Fetching expenses for month={}", month);

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

        logger.info("Found {} expenses for month={}", expenses.size(), month);
        return resp;
    }

    @Override
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {

        logger.info("Updating expense id={}", id);

        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Expense not found for update, id={}", id);
                    return new ExpenseNotFoundException("Expense not found");
                });

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        Expense updated = expenseRepo.save(expense);

        logger.info("Expense updated successfully, id={}", id);
        return mapToResponse(updated);
    }

    @Override
    public void disableExpense(Long id) {

        logger.info("Disabling expense id={}", id);

        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Expense not found for disable, id={}", id);
                    return new ExpenseNotFoundException("Expense not found");
                });

        expense.setActive(false);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(currentUser.getCurrentUser());

        expenseRepo.save(expense);

        logger.info("Expense disabled successfully, id={}", id);
    }

    // Local mapper is perfectly acceptable here
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
