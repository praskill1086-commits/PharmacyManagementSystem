package com.ey.service;

import java.time.LocalDate;
import java.util.List;

import com.ey.dto.request.CreateAlertRequest;
import com.ey.dto.response.AlertResponse;

public interface AlertService {

    AlertResponse createAlert(CreateAlertRequest request);

    List<AlertResponse> getAllAlerts();

    List<AlertResponse> getDueAlerts(LocalDate date);

    void disableAlert(Long id);
}


