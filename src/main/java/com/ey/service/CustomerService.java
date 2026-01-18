package com.ey.service;

import com.ey.dto.request.CreateCustomerRequest;
import com.ey.dto.request.UpdateCustomerRequest;
import com.ey.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest request);
    List<CustomerResponse> getAll();
    CustomerResponse getById(Long id);
    CustomerResponse update(Long id, UpdateCustomerRequest request);
    void disable(Long id);
}