package com.ey.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaleItemRequest {
    @NotNull
    private Long medicineId;

    @Positive
    private int quantity;
    
    
    

	public SaleItemRequest(@NotNull Long medicineId, @Positive int quantity) {
		super();
		this.medicineId = medicineId;
		this.quantity = quantity;
	}

	public SaleItemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

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
