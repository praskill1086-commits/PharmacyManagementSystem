package com.ey.controller;

import com.ey.dto.request.*;
import com.ey.dto.response.MedicineResponse;
import com.ey.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    private final MedicineService service;

    public MedicineController(MedicineService service) {
        this.service = service;
    }

    @PostMapping
    public MedicineResponse create(@Valid @RequestBody CreateMedicineRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<MedicineResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MedicineResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public MedicineResponse update(@PathVariable Long id, @Valid @RequestBody UpdateMedicineRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        service.disable(id);
    }

    @PostMapping("/add-stock")
    public MedicineResponse addStock(@Valid @RequestBody AddStockRequest request) {
        return service.addStock(request);
    }

    @GetMapping("/low-stock")
    public List<MedicineResponse> lowStock() {
        return service.getLowStock();
    }

    @GetMapping("/near-expiry")
    public List<MedicineResponse> nearExpiry() {
        return service.getNearExpiry();
    }

    @GetMapping("/expired")
    public List<MedicineResponse> expired() {
        return service.getExpired();
    }
}
