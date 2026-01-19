package com.ey.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateSaleRequest;
import com.ey.dto.request.SaleItemRequest;
import com.ey.dto.response.SaleResponse;
import com.ey.entity.Customer;
import com.ey.entity.Medicine;
import com.ey.entity.Offer;
import com.ey.entity.Sale;
import com.ey.entity.User;
import com.ey.entity.Wallet;
import com.ey.enums.WalletTransactionType;
import com.ey.repository.CustomerRepository;
import com.ey.repository.MedicineRepository;
import com.ey.repository.OfferRepository;
import com.ey.repository.SaleItemRepository;
import com.ey.repository.SaleRepository;
import com.ey.repository.WalletRepository;
import com.ey.repository.WalletTransactionRepository;

class SaleServiceImplTest {

    @Mock
    private MedicineRepository medicineRepo;

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private SaleRepository saleRepo;

    @Mock
    private SaleItemRepository saleItemRepo;

    @Mock
    private WalletRepository walletRepo;

    @Mock
    private WalletTransactionRepository walletTxnRepo;

    @Mock
    private OfferRepository offerRepo;

    @Mock
    private CurrentUserUtil currentUser;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Customer customer;
    private Medicine medicine;
    private User staff;
    private Wallet wallet;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        staff = new User();
        staff.setId(1L);
        staff.setUsername("staff1");

        customer = new Customer();
        customer.setId(1L);
        customer.setActive(true);

        medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Paracetamol");
        medicine.setPrice(10);
        medicine.setQuantity(100);
        medicine.setExpiryDate(LocalDate.now().plusMonths(6));

        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setCustomer(customer);
        wallet.setBalance(50);

        when(currentUser.getCurrentUser()).thenReturn(staff);
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(medicineRepo.findById(1L)).thenReturn(Optional.of(medicine));
        when(saleRepo.save(any(Sale.class))).thenAnswer(i -> i.getArgument(0));
    }

    // -------------------------
    // SUCCESS – SALE WITH OFFER
    // -------------------------
    @Test
    void testCreateSale_WithOffer() {

        Offer offer = new Offer();
        offer.setDiscountPercent(10);

        when(offerRepo.findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                any(), any()))
                .thenReturn(Optional.of(offer));

        when(walletRepo.findByCustomerId(1L))
                .thenReturn(Optional.of(wallet));

        CreateSaleRequest req = new CreateSaleRequest();
        req.setCustomerId(1L);
        req.setUseWallet(false);
        req.setItems(List.of(new SaleItemRequest(1L, 2)));

        SaleResponse resp = saleService.createSale(req);

        assertNotNull(resp);
        assertEquals(20, resp.getSubtotal());
        assertEquals(2, resp.getDiscount());
        assertEquals(18, resp.getFinalAmount());
        assertTrue(resp.getCashbackEarned() > 0);

        verify(walletRepo, times(1)).save(any(Wallet.class));
        verify(walletTxnRepo, times(1)).save(any());
    }

    // -------------------------
    // SUCCESS – SALE WITH WALLET
    // -------------------------
    @Test
    void testCreateSale_UsingWallet() {

        when(offerRepo.findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                any(), any()))
                .thenReturn(Optional.empty());

        when(walletRepo.findByCustomerId(1L))
                .thenReturn(Optional.of(wallet));

        CreateSaleRequest req = new CreateSaleRequest();
        req.setCustomerId(1L);
        req.setUseWallet(true);
        req.setItems(List.of(new SaleItemRequest(1L, 3)));

        SaleResponse resp = saleService.createSale(req);

        assertNotNull(resp);
        assertEquals(30, resp.getSubtotal());
        assertEquals(0, resp.getDiscount());
        assertTrue(resp.getWalletUsed() > 0);
       

        // wallet saved twice → redeem + cashback
        verify(walletRepo, times(2)).save(any(Wallet.class));
        verify(walletTxnRepo, times(2)).save(any());
    }

    // -------------------------
    // FAILURE – INSUFFICIENT STOCK
    // -------------------------
    @Test
    void testCreateSale_InsufficientStock() {

        medicine.setQuantity(1); // insufficient

        CreateSaleRequest req = new CreateSaleRequest();
        req.setCustomerId(1L);
        req.setUseWallet(false);
        req.setItems(List.of(new SaleItemRequest(1L, 5)));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> saleService.createSale(req));

        assertTrue(ex.getMessage().contains("Not enough stock"));
        verify(saleRepo, never()).save(any());
    }
}
