package com.ey.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

import com.ey.dto.request.CreateAlertRequest;
import com.ey.dto.response.AlertResponse;
import com.ey.service.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponse> create(
            @Valid @RequestBody CreateAlertRequest request) {
        return new ResponseEntity<>(
                alertService.createAlert(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAll() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/due")
    public ResponseEntity<List<AlertResponse>> getDue(
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(alertService.getDueAlerts(date));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        alertService.disableAlert(id);
        return ResponseEntity.ok("Alert disabled successfully");
    }
}

