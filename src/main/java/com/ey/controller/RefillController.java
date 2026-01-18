package com.ey.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CreateRefillRequest;
import com.ey.dto.request.UpdateRefillRequest;
import com.ey.dto.response.RefillResponse;
import com.ey.service.RefillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/refills")
public class RefillController {

    @Autowired
    private RefillService refillService;

    @PostMapping
    public ResponseEntity<RefillResponse> create(
            @Valid @RequestBody CreateRefillRequest request) {
        return new ResponseEntity<>(
                refillService.createRefill(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<RefillResponse>> getAll() {
        return ResponseEntity.ok(refillService.getAllRefills());
    }

    @GetMapping("/due")
    public ResponseEntity<List<RefillResponse>> getDue(
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(refillService.getDueRefills(date));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<RefillResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(refillService.completeRefill(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RefillResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRefillRequest request) {
        return ResponseEntity.ok(refillService.updateRefill(id, request));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        refillService.disableRefill(id);
        return ResponseEntity.ok(
               "Refill plan disabled successfully"
        );
    }
}

