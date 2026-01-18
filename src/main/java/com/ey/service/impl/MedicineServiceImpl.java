package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.MedicineResponse;
import com.ey.entity.Dealer;
import com.ey.entity.Medicine;
import com.ey.mapper.MedicineMapper;
import com.ey.repository.DealerRepository;
import com.ey.repository.MedicineRepository;
import com.ey.service.MedicineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepo;
    private final DealerRepository dealerRepo;
    private final CurrentUserUtil currentUser;

    public MedicineServiceImpl(MedicineRepository medicineRepo, DealerRepository dealerRepo, CurrentUserUtil currentUser) {
        this.medicineRepo = medicineRepo;
        this.dealerRepo = dealerRepo;
        this.currentUser = currentUser;
    }

    @Override
    public MedicineResponse create(CreateMedicineRequest request) {
        Dealer dealer = dealerRepo.findById(request.getDealerId()).orElseThrow();

        Medicine m = new Medicine();
        m.setName(request.getName());
        m.setBatchNumber(request.getBatchNumber());
        m.setExpiryDate(request.getExpiryDate());
        m.setPrice(request.getPrice());
        m.setQuantity(request.getQuantity());
        m.setDealer(dealer);
        m.setActive(true);
        m.setCreatedAt(LocalDateTime.now());
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());

        return MedicineMapper.toResponse(medicineRepo.save(m));
    }

    @Override
    public List<MedicineResponse> getAll() {
        return medicineRepo.findByActiveTrue().stream().map(MedicineMapper::toResponse).toList();
    }

    @Override
    public MedicineResponse getById(Long id) {
        return MedicineMapper.toResponse(medicineRepo.findById(id).orElseThrow());
    }

    @Override
    public MedicineResponse update(Long id, UpdateMedicineRequest request) {
        Medicine m = medicineRepo.findById(id).orElseThrow();
        Dealer dealer = dealerRepo.findById(request.getDealerId()).orElseThrow();

        m.setName(request.getName());
        m.setBatchNumber(request.getBatchNumber());
        m.setExpiryDate(request.getExpiryDate());
        m.setPrice(request.getPrice());
        m.setQuantity(request.getQuantity());
        m.setDealer(dealer);
        m.setActive(request.isActive());
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());

        return MedicineMapper.toResponse(medicineRepo.save(m));
    }

    @Override
    public void disable(Long id) {
        Medicine m = medicineRepo.findById(id).orElseThrow();
        m.setActive(false);
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());
        medicineRepo.save(m);
    }

    @Override
    public MedicineResponse addStock(AddStockRequest request) {
        Medicine m = medicineRepo.findById(request.getMedicineId()).orElseThrow();
        m.setQuantity(m.getQuantity() + request.getQuantity());
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());
        return MedicineMapper.toResponse(medicineRepo.save(m));
    }

    @Override
    public List<MedicineResponse> getLowStock() {
        return medicineRepo.findByQuantityLessThan(10).stream().map(MedicineMapper::toResponse).toList();
    }

    @Override
    public List<MedicineResponse> getNearExpiry() {
        return medicineRepo.findByExpiryDateBetween(LocalDate.now(), LocalDate.now().plusDays(30))
                .stream().map(MedicineMapper::toResponse).toList();
    }

    @Override
    public List<MedicineResponse> getExpired() {
        return medicineRepo.findByExpiryDateBefore(LocalDate.now())
                .stream().map(MedicineMapper::toResponse).toList();
    }
}
