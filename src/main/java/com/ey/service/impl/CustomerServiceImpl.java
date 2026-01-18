package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.entity.Customer;
import com.ey.mapper.CustomerMapper;
import com.ey.repository.CustomerRepository;
import com.ey.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;
    private final CurrentUserUtil currentUser;

    public CustomerServiceImpl(CustomerRepository repo, CurrentUserUtil currentUser) {
        this.repo = repo;
        this.currentUser = currentUser;
    }

    @Override
    public CustomerResponse create(CreateCustomerRequest request) {
        Customer c = CustomerMapper.toEntity(request);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        c.setUpdatedBy(currentUser.getCurrentUser());
        return CustomerMapper.toResponse(repo.save(c));
    }

    @Override
    public List<CustomerResponse> getAll() {
        return repo.findByActiveTrue().stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public CustomerResponse getById(Long id) {
        return CustomerMapper.toResponse(repo.findById(id).orElseThrow());
    }

    @Override
    public CustomerResponse update(Long id, UpdateCustomerRequest request) {
        Customer c = repo.findById(id).orElseThrow();
        c.setName(request.getName());
        c.setPhone(request.getPhone());
        c.setActive(request.isActive());
        c.setUpdatedAt(LocalDateTime.now());
        c.setUpdatedBy(currentUser.getCurrentUser());
        return CustomerMapper.toResponse(repo.save(c));
    }

    @Override
    public void disable(Long id) {
        Customer c = repo.findById(id).orElseThrow();
        c.setActive(false);
        c.setUpdatedAt(LocalDateTime.now());
        c.setUpdatedBy(currentUser.getCurrentUser());
        repo.save(c);
    }
}
