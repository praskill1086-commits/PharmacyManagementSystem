package com.ey.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateDealerRequest;
import com.ey.dto.request.UpdateDealerRequest;
import com.ey.dto.response.DealerResponse;
import com.ey.entity.Dealer;
import com.ey.entity.User;
import com.ey.repository.DealerRepository;

@ExtendWith(MockitoExtension.class)
class DealerServiceImplTest {

    @Mock
    private DealerRepository dealerRepo;

    @Mock
    private CurrentUserUtil currentUser;

    @InjectMocks
    private DealerServiceImpl dealerService;

    private Dealer dealer;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("admin");

        dealer = new Dealer();
        dealer.setId(1L);
        dealer.setName("ABC Pharma");
        dealer.setEmail("abc@pharma.com");
        dealer.setPhone("8888888888");
        dealer.setAddress("Hyderabad");
        dealer.setActive(true);
    }

    @Test
    void testCreateDealer() {
        CreateDealerRequest request = new CreateDealerRequest();
        request.setName("ABC Pharma");
        request.setEmail("abc@pharma.com");
        request.setPhone("8888888888");
        request.setAddress("Hyderabad");

        when(currentUser.getCurrentUser()).thenReturn(user);
        when(dealerRepo.save(any(Dealer.class))).thenReturn(dealer);

        DealerResponse response = dealerService.create(request);

        assertNotNull(response);
        assertEquals("ABC Pharma", response.getName());
        verify(dealerRepo).save(any(Dealer.class));
    }

    @Test
    void testGetAllDealers() {
        when(dealerRepo.findByActiveTrue()).thenReturn(List.of(dealer));

        List<DealerResponse> dealers = dealerService.getAll();

        assertEquals(1, dealers.size());
        assertEquals("ABC Pharma", dealers.get(0).getName());
    }

    @Test
    void testGetDealerById() {
        when(dealerRepo.findById(1L)).thenReturn(Optional.of(dealer));

        DealerResponse response = dealerService.getById(1L);

        assertEquals("ABC Pharma", response.getName());
    }

    @Test
    void testUpdateDealer() {
        UpdateDealerRequest request = new UpdateDealerRequest();
        request.setName("Updated Pharma");
        request.setEmail("updated@pharma.com");
        request.setPhone("7777777777");
        request.setAddress("Mumbai");
        request.setActive(true);

        when(dealerRepo.findById(1L)).thenReturn(Optional.of(dealer));
        when(currentUser.getCurrentUser()).thenReturn(user);
        when(dealerRepo.save(any(Dealer.class))).thenReturn(dealer);

        DealerResponse response = dealerService.update(1L, request);

        assertEquals("Updated Pharma", response.getName());
        verify(dealerRepo).save(dealer);
    }

    @Test
    void testDisableDealer() {
        when(dealerRepo.findById(1L)).thenReturn(Optional.of(dealer));

        dealerService.disable(1L);

        assertFalse(dealer.isActive());
        verify(dealerRepo).save(dealer);
    }
}
