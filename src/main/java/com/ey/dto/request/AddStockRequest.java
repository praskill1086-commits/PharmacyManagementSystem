package com.ey.dto.request;

import jakarta.validation.constraints.*;

public class AddStockRequest {

    @NotNull
    private Long medicineId;

    @Positive
    private int quantity;

	public Long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Long medicineId) {
		this.medicineId = medicineId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    
}
