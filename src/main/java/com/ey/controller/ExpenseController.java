package com.ey.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CreateExpenseRequest;
import com.ey.dto.request.UpdateExpenseRequest;
import com.ey.dto.response.ExpenseMonthResponse;
import com.ey.dto.response.ExpenseResponse;
import com.ey.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(
            @Valid @RequestBody CreateExpenseRequest request) {
        return new ResponseEntity<>(
                expenseService.createExpense(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAll() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping(params = "month")
    public ResponseEntity<ExpenseMonthResponse> getByMonth(
            @RequestParam String month) {
        return ResponseEntity.ok(expenseService.getExpensesByMonth(month));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        expenseService.disableExpense(id);
        return ResponseEntity.ok(
                Map.of("message", "Expense disabled successfully")
        );
    }
}

