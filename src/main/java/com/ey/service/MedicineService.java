package com.ey.service;

import com.ey.dto.request.*;
import com.ey.dto.response.MedicineResponse;
import java.util.List;

public interface MedicineService {
    MedicineResponse create(CreateMedicineRequest request);
    List<MedicineResponse> getAll();
    MedicineResponse getById(Long id);
    MedicineResponse update(Long id, UpdateMedicineRequest request);
    void disable(Long id);
    MedicineResponse addStock(AddStockRequest request);

    List<MedicineResponse> getLowStock();
    List<MedicineResponse> getNearExpiry();
    List<MedicineResponse> getExpired();
}
