package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateAlertRequest;
import com.ey.dto.response.AlertResponse;
import com.ey.entity.Alert;
import com.ey.repository.AlertRepository;
import com.ey.service.AlertService;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private AlertRepository alertRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public AlertResponse createAlert(CreateAlertRequest request) {
        Alert alert = new Alert();
        alert.setMessage(request.getMessage());
        alert.setDueDate(request.getDueDate());
        alert.setType(request.getType());
        alert.setActive(true);
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setUpdatedBy(currentUser.getCurrentUser());

        return map(alertRepo.save(alert));
    }

    @Override
    public List<AlertResponse> getAllAlerts() {
        return alertRepo.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getDueAlerts(LocalDate date) {
        return alertRepo.findByActiveTrueAndDueDateLessThanEqual(date)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public void disableAlert(Long id) {
        Alert alert = alertRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setActive(false);
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setUpdatedBy(currentUser.getCurrentUser());

        alertRepo.save(alert);
    }

    private AlertResponse map(Alert a) {
        AlertResponse r = new AlertResponse();
        r.setId(a.getId());
        r.setMessage(a.getMessage());
        r.setDueDate(a.getDueDate());
        r.setType(a.getType());
        r.setActive(a.isActive());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        r.setUpdatedBy(
            a.getUpdatedBy() != null ? a.getUpdatedBy().getUsername() : null
        );
        return r;
    }
}
