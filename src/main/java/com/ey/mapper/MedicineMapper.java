package com.ey.mapper;

import com.ey.dto.response.MedicineResponse;
import com.ey.entity.Medicine;

public class MedicineMapper {

    public static MedicineResponse toResponse(Medicine m) {
        MedicineResponse r = new MedicineResponse();
        r.setId(m.getId());
        r.setName(m.getName());
        r.setBatchNumber(m.getBatchNumber());
        r.setExpiryDate(m.getExpiryDate());
        r.setPrice(m.getPrice());
        r.setQuantity(m.getQuantity());
        r.setDealerName(m.getDealer().getName());
        r.setActive(m.isActive());
        r.setCreatedAt(m.getCreatedAt());
        r.setUpdatedAt(m.getUpdatedAt());
        r.setUpdatedBy(m.getUpdatedBy() != null ? m.getUpdatedBy().getUsername() : null);
        return r;
    }
}
