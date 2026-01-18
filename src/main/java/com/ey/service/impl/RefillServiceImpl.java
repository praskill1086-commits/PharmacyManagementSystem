package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateRefillRequest;
import com.ey.dto.request.UpdateRefillRequest;
import com.ey.dto.response.RefillResponse;
import com.ey.entity.RefillPlan;
import com.ey.repository.CustomerRepository;
import com.ey.repository.MedicineRepository;
import com.ey.repository.RefillPlanRepository;
import com.ey.service.RefillService;

@Service
public class RefillServiceImpl implements RefillService {

    @Autowired
    private RefillPlanRepository refillRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public RefillResponse createRefill(CreateRefillRequest request) {
        RefillPlan refill = new RefillPlan();
        refill.setCustomer(customerRepo.findById(request.getCustomerId()).orElseThrow());
        refill.setMedicine(medicineRepo.findById(request.getMedicineId()).orElseThrow());
        refill.setQuantity(request.getQuantity());
        refill.setIntervalDays(request.getIntervalDays());
        refill.setNextRefillDate(request.getNextRefillDate());
        refill.setActive(true);
        refill.setCreatedAt(LocalDateTime.now());
        refill.setUpdatedAt(LocalDateTime.now());
        refill.setUpdatedBy(currentUser.getCurrentUser());

        return map(refillRepo.save(refill));
    }

    @Override
    public List<RefillResponse> getAllRefills() {
        return refillRepo.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefillResponse> getDueRefills(LocalDate date) {
        return refillRepo.findByActiveTrueAndNextRefillDateLessThanEqual(date)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public RefillResponse completeRefill(Long id) {
        RefillPlan refill = refillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Refill not found"));

        refill.setNextRefillDate(
                refill.getNextRefillDate().plusDays(refill.getIntervalDays())
        );
        refill.setUpdatedAt(LocalDateTime.now());
        refill.setUpdatedBy(currentUser.getCurrentUser());

        return map(refillRepo.save(refill));
    }

    @Override
    public RefillResponse updateRefill(Long id, UpdateRefillRequest request) {
        RefillPlan refill = refillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Refill not found"));

        refill.setQuantity(request.getQuantity());
        refill.setIntervalDays(request.getIntervalDays());
        refill.setNextRefillDate(request.getNextRefillDate());
        refill.setUpdatedAt(LocalDateTime.now());
        refill.setUpdatedBy(currentUser.getCurrentUser());

        return map(refillRepo.save(refill));
    }

    @Override
    public void disableRefill(Long id) {
        RefillPlan refill = refillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Refill not found"));

        refill.setActive(false);
        refill.setUpdatedAt(LocalDateTime.now());
        refill.setUpdatedBy(currentUser.getCurrentUser());

        refillRepo.save(refill);
    }

    private RefillResponse map(RefillPlan r) {
        RefillResponse resp = new RefillResponse();
        resp.setId(r.getId());
        resp.setCustomerName(r.getCustomer().getName());
        resp.setMedicineName(r.getMedicine().getName());
        resp.setQuantity(r.getQuantity());
        resp.setIntervalDays(r.getIntervalDays());
        resp.setNextRefillDate(r.getNextRefillDate());
        resp.setActive(r.isActive());
        resp.setCreatedAt(r.getCreatedAt());
        resp.setUpdatedAt(r.getUpdatedAt());
        resp.setUpdatedBy(
            r.getUpdatedBy() != null ? r.getUpdatedBy().getUsername() : null
        );
        return resp;
    }
}

