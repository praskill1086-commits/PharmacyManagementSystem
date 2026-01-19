package com.ey.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateCustomerRequest;
import com.ey.dto.request.UpdateCustomerRequest;
import com.ey.dto.response.CustomerResponse;
import com.ey.entity.Customer;
import com.ey.entity.User;
import com.ey.repository.CustomerRepository;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private CurrentUserUtil currentUser;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private User loggedInUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("admin");

        when(currentUser.getCurrentUser()).thenReturn(loggedInUser);
    }

    // ================= CREATE =================

    @Test
    void createCustomer_success() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("John");
        request.setPhone("9999999999");

        Customer saved = new Customer();
        saved.setId(1L);
        saved.setName("John");
        saved.setPhone("9999999999");
        saved.setActive(true);
        saved.setUpdatedBy(loggedInUser);

        when(customerRepo.save(any(Customer.class))).thenReturn(saved);

        CustomerResponse response = customerService.create(request);

        assertNotNull(response);
        assertEquals("John", response.getName());
        assertEquals("9999999999", response.getPhone());
        assertEquals("admin", response.getUpdatedBy());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    // ================= GET ALL =================

    @Test
    void getAll_activeCustomersOnly() {
        Customer c1 = new Customer();
        c1.setId(1L);
        c1.setName("A");
        c1.setActive(true);

        Customer c2 = new Customer();
        c2.setId(2L);
        c2.setName("B");
        c2.setActive(true);

        when(customerRepo.findByActiveTrue()).thenReturn(List.of(c1, c2));

        List<CustomerResponse> list = customerService.getAll();

        assertEquals(2, list.size());
        verify(customerRepo).findByActiveTrue();
    }

    // ================= GET BY ID =================

    @Test
    void getById_success() {
        Customer c = new Customer();
        c.setId(1L);
        c.setName("John");

        when(customerRepo.findById(1L)).thenReturn(Optional.of(c));

        CustomerResponse response = customerService.getById(1L);

        assertEquals("John", response.getName());
    }

    @Test
    void getById_notFound() {
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> customerService.getById(99L)
        );

        assertNotNull(ex);
    }

    // ================= UPDATE =================

    @Test
    void updateCustomer_success() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setName("Old");
        existing.setPhone("111");
        existing.setActive(true);

        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("New");
        request.setPhone("222");
        request.setActive(true);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepo.save(any(Customer.class))).thenReturn(existing);

        CustomerResponse response = customerService.update(1L, request);

        assertEquals("New", response.getName());
        assertEquals("222", response.getPhone());
        verify(customerRepo).save(existing);
    }

    // ================= DISABLE =================

    @Test
    void disableCustomer_success() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setActive(true);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(existing));

        customerService.disable(1L);

        assertFalse(existing.isActive());
        assertEquals("admin", existing.getUpdatedBy().getUsername());
        verify(customerRepo).save(existing);
    }

    @Test
    void disableCustomer_notFound() {
        when(customerRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> customerService.disable(10L)
        );

        assertNotNull(ex);
    }
}
