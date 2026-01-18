package com.ey.mapper;

import com.ey.dto.request.CreateCustomerRequest;
import com.ey.dto.response.CustomerResponse;
import com.ey.entity.Customer;

public class CustomerMapper {

    public static Customer toEntity(CreateCustomerRequest dto) {
        Customer c = new Customer();
        c.setName(dto.getName());
        c.setPhone(dto.getPhone());
        c.setActive(true);
        return c;
    }

    public static CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setPhone(c.getPhone());
        r.setActive(c.isActive());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        r.setUpdatedBy(c.getUpdatedBy() != null ? c.getUpdatedBy().getUsername() : null);
        return r;
    }
}
