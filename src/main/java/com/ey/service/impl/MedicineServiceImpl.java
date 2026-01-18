package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.MedicineResponse;
import com.ey.entity.Dealer;
import com.ey.entity.Medicine;
import com.ey.exception.MedicineOperationException;
import com.ey.mapper.MedicineMapper;
import com.ey.repository.DealerRepository;
import com.ey.repository.MedicineRepository;
import com.ey.service.MedicineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {

    private static final Logger logger =
            LoggerFactory.getLogger(MedicineServiceImpl.class);

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private DealerRepository dealerRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public MedicineResponse create(CreateMedicineRequest request) {

        if (medicineRepo.existsByNameAndBatchNumberAndActiveTrue(
                request.getName(), request.getBatchNumber())) {
            throw new MedicineOperationException("Medicine with same name and batch already exists");
        }

        Dealer dealer = dealerRepo.findById(request.getDealerId())
                .orElseThrow(() -> new RuntimeException("Dealer not found"));

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

        Medicine saved = medicineRepo.save(m);
        logger.info("Medicine created with id {}", saved.getId());

        return MedicineMapper.toResponse(saved);
    }

    @Override
    public List<MedicineResponse> getAll() {
        return medicineRepo.findByActiveTrue()
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }

    @Override
    public MedicineResponse getById(Long id) {
        Medicine m = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return MedicineMapper.toResponse(m);
    }

    @Override
    public MedicineResponse update(Long id, UpdateMedicineRequest request) {

        Medicine m = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (!m.getName().equalsIgnoreCase(request.getName())
                || !m.getBatchNumber().equals(request.getBatchNumber())) {

            if (medicineRepo.existsByNameAndBatchNumberAndActiveTrue(
                    request.getName(), request.getBatchNumber())) {
                throw new MedicineOperationException(
                        "Another active medicine with same name and batch exists");
            }
        }

        Dealer dealer = dealerRepo.findById(request.getDealerId())
                .orElseThrow(() -> new RuntimeException("Dealer not found"));

        m.setName(request.getName());
        m.setBatchNumber(request.getBatchNumber());
        m.setExpiryDate(request.getExpiryDate());
        m.setPrice(request.getPrice());
        m.setQuantity(request.getQuantity());
        m.setDealer(dealer);
        m.setActive(request.isActive());
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());

        Medicine saved = medicineRepo.save(m);
        logger.info("Medicine updated with id {}", saved.getId());

        return MedicineMapper.toResponse(saved);
    }

    @Override
    public void disable(Long id) {

        Medicine m = medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (!m.isActive()) {
            throw new RuntimeException("Medicine already disabled");
        }

        m.setActive(false);
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());

        medicineRepo.save(m);
        logger.info("Medicine disabled with id {}", id);
    }

    @Override
    public MedicineResponse addStock(AddStockRequest request) {

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Stock quantity must be positive");
        }

        Medicine m = medicineRepo.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        m.setQuantity(m.getQuantity() + request.getQuantity());
        m.setUpdatedAt(LocalDateTime.now());
        m.setUpdatedBy(currentUser.getCurrentUser());

        Medicine saved = medicineRepo.save(m);
        logger.info("Stock added for medicine id {}", saved.getId());

        return MedicineMapper.toResponse(saved);
    }

    @Override
    public List<MedicineResponse> getLowStock() {
        return medicineRepo.findByQuantityLessThan(10)
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }

    @Override
    public List<MedicineResponse> getNearExpiry() {
        return medicineRepo.findByExpiryDateBetween(
                        LocalDate.now(), LocalDate.now().plusDays(30))
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }

    @Override
    public List<MedicineResponse> getExpired() {
        return medicineRepo.findByExpiryDateBefore(LocalDate.now())
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }
}
