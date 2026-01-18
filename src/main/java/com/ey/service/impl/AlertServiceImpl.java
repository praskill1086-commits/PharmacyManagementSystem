package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateAlertRequest;
import com.ey.dto.response.AlertResponse;
import com.ey.entity.Alert;
import com.ey.exception.AlertNotFoundException;
import com.ey.repository.AlertRepository;
import com.ey.service.AlertService;

@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger logger =
            LoggerFactory.getLogger(AlertServiceImpl.class);

    @Autowired
    private AlertRepository alertRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public AlertResponse createAlert(CreateAlertRequest request) {

        logger.info("Creating alert with message={}", request.getMessage());

        Alert alert = new Alert();
        alert.setMessage(request.getMessage());
        alert.setDueDate(request.getDueDate());
        alert.setType(request.getType());
        alert.setActive(true);
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setUpdatedBy(currentUser.getCurrentUser());

        Alert saved = alertRepo.save(alert);

        logger.info("Alert created successfully, id={}", saved.getId());
        return map(saved);
    }

    @Override
    public List<AlertResponse> getAllAlerts() {

        logger.info("Fetching all active alerts");

        return alertRepo.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getDueAlerts(LocalDate date) {

        logger.info("Fetching due alerts up to date={}", date);

        return alertRepo.findByActiveTrueAndDueDateLessThanEqual(date)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public void disableAlert(Long id) {

        logger.info("Disabling alert id={}", id);

        Alert alert = alertRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Alert not found for disable, id={}", id);
                    return new AlertNotFoundException("Alert not found");
                });

        alert.setActive(false);
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setUpdatedBy(currentUser.getCurrentUser());

        alertRepo.save(alert);

        logger.info("Alert disabled successfully, id={}", id);
    }

    // Local mapper is perfectly fine here
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
