package com.ey.service;

import java.time.LocalDate;
import java.util.List;
import com.ey.dto.request.*;
import com.ey.dto.response.RefillResponse;

public interface RefillService {

    RefillResponse createRefill(CreateRefillRequest request);

    List<RefillResponse> getAllRefills();

    List<RefillResponse> getDueRefills(LocalDate date);

    RefillResponse updateRefill(Long id, UpdateRefillRequest request);

    RefillResponse completeRefill(Long id);

    void disableRefill(Long id);
}

