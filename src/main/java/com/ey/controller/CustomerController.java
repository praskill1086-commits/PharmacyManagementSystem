package com.ey.controller;

import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<CustomerResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        service.disable(id);
    }
}
