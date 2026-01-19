package com.ey.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.AddStockRequest;
import com.ey.dto.request.CreateMedicineRequest;
import com.ey.dto.request.UpdateMedicineRequest;
import com.ey.dto.response.MedicineResponse;
import com.ey.entity.Dealer;
import com.ey.entity.Medicine;
import com.ey.entity.User;
import com.ey.repository.DealerRepository;
import com.ey.repository.MedicineRepository;

@ExtendWith(MockitoExtension.class)
class MedicineServiceImplTest {

    @Mock
    private MedicineRepository medicineRepo;

    @Mock
    private DealerRepository dealerRepo;

    @Mock
    private CurrentUserUtil currentUser;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    private Medicine medicine;
    private Dealer dealer;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("staff");

        dealer = new Dealer();
        dealer.setId(1L);
        dealer.setName("ABC Pharma");

        medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Paracetamol");
        medicine.setBatchNumber("B123");
        medicine.setPrice(10.0);
        medicine.setQuantity(50);
        medicine.setExpiryDate(LocalDate.now().plusMonths(6));
        medicine.setDealer(dealer);
        medicine.setActive(true);
    }

    // ================= CREATE =================
    @Test
    void testCreateMedicine() {
        CreateMedicineRequest request = new CreateMedicineRequest();
        request.setName("Paracetamol");
        request.setBatchNumber("B123");
        request.setPrice(10.0);
        request.setQuantity(50);
        request.setExpiryDate(LocalDate.now().plusMonths(6));
        request.setDealerId(1L);

        when(dealerRepo.findById(1L)).thenReturn(Optional.of(dealer));
        when(currentUser.getCurrentUser()).thenReturn(user);
        when(medicineRepo.save(any(Medicine.class))).thenReturn(medicine);

        MedicineResponse response = medicineService.create(request);

        assertNotNull(response);
        assertEquals("Paracetamol", response.getName());
        assertEquals(50, response.getQuantity());
        verify(medicineRepo).save(any(Medicine.class));
    }

    // ================= GET ALL =================
    @Test
    void testGetAllMedicines() {
        when(medicineRepo.findByActiveTrue()).thenReturn(List.of(medicine));

        List<MedicineResponse> list = medicineService.getAll();

        assertEquals(1, list.size());
        assertEquals("Paracetamol", list.get(0).getName());
    }

    // ================= GET BY ID =================
    @Test
    void testGetMedicineById() {
        when(medicineRepo.findById(1L)).thenReturn(Optional.of(medicine));

        MedicineResponse response = medicineService.getById(1L);

        assertEquals("Paracetamol", response.getName());
    }

    // ================= UPDATE =================
    @Test
    void testUpdateMedicine() {
        UpdateMedicineRequest request = new UpdateMedicineRequest();
        request.setName("Updated Med");
        request.setBatchNumber("NEWBATCH");
        request.setPrice(15.0);
        request.setQuantity(30);
        request.setExpiryDate(LocalDate.now().plusMonths(12));
        request.setDealerId(1L);
        request.setActive(true);

        when(medicineRepo.findById(1L)).thenReturn(Optional.of(medicine));
        when(dealerRepo.findById(1L)).thenReturn(Optional.of(dealer));
        when(currentUser.getCurrentUser()).thenReturn(user);
        when(medicineRepo.save(any(Medicine.class))).thenReturn(medicine);

        MedicineResponse response = medicineService.update(1L, request);

        assertEquals("Updated Med", response.getName());
        verify(medicineRepo).save(medicine);
    }

    // ================= DISABLE =================
    @Test
    void testDisableMedicine() {
        when(medicineRepo.findById(1L)).thenReturn(Optional.of(medicine));

        medicineService.disable(1L);

        assertFalse(medicine.isActive());
        verify(medicineRepo).save(medicine);
    }

    // ================= ADD STOCK =================
    @Test
    void testAddStock() {
        AddStockRequest request = new AddStockRequest();
        request.setMedicineId(1L);
        request.setQuantity(20);

        when(medicineRepo.findById(1L)).thenReturn(Optional.of(medicine));
        when(currentUser.getCurrentUser()).thenReturn(user);
        when(medicineRepo.save(any(Medicine.class))).thenReturn(medicine);

        MedicineResponse response = medicineService.addStock(request);

        assertEquals(70, medicine.getQuantity());
        verify(medicineRepo).save(medicine);
    }

    // ================= LOW STOCK =================
    @Test
    void testGetLowStock() {
        medicine.setQuantity(5);
        when(medicineRepo.findByQuantityLessThan(10))
                .thenReturn(List.of(medicine));

        List<MedicineResponse> list = medicineService.getLowStock();

        assertEquals(1, list.size());
        assertTrue(list.get(0).getQuantity() < 10);
    }

    // ================= NEAR EXPIRY =================
    @Test
    void testGetNearExpiry() {
        when(medicineRepo.findByExpiryDateBetween(any(), any()))
                .thenReturn(List.of(medicine));

        List<MedicineResponse> list = medicineService.getNearExpiry();

        assertFalse(list.isEmpty());
    }

    // ================= EXPIRED =================
    @Test
    void testGetExpiredMedicines() {
        medicine.setExpiryDate(LocalDate.now().minusDays(1));
        when(medicineRepo.findByExpiryDateBefore(any()))
                .thenReturn(List.of(medicine));

        List<MedicineResponse> list = medicineService.getExpired();

        assertEquals(1, list.size());
        assertTrue(list.get(0).getExpiryDate().isBefore(LocalDate.now()));
    }
}
