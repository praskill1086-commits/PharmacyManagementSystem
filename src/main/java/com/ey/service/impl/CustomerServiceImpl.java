package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateCustomerRequest;
import com.ey.dto.request.UpdateCustomerRequest;
import com.ey.dto.response.CustomerResponse;
import com.ey.entity.Customer;
import com.ey.exception.CustomerOperationException;
import com.ey.mapper.CustomerMapper;
import com.ey.repository.CustomerRepository;
import com.ey.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public CustomerResponse create(CreateCustomerRequest request) {

        if (repo.existsByPhone(request.getPhone())) {
            logger.warn("Customer with phone already exists: {}", request.getPhone());
            throw new CustomerOperationException("Customer with this phone already exists");
        }

        Customer customer = CustomerMapper.toEntity(request);
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(currentUser.getCurrentUser());

        Customer saved = repo.save(customer);
        logger.info("Customer created with id {}", saved.getId());

        return CustomerMapper.toResponse(saved);
    }

    @Override
    public List<CustomerResponse> getAll() {
        logger.info("Fetching all active customers");
        return repo.findByActiveTrue()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse getById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse update(Long id, UpdateCustomerRequest request) {

        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setActive(request.isActive());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(currentUser.getCurrentUser());

        Customer saved = repo.save(customer);
        logger.info("Customer updated with id {}", saved.getId());

        return CustomerMapper.toResponse(saved);
    }

    @Override
    public void disable(Long id) {

        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.isActive()) {
            throw new RuntimeException("Customer already disabled");
        }

        customer.setActive(false);
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(currentUser.getCurrentUser());

        repo.save(customer);
        logger.info("Customer disabled with id {}", id);
    }
}
