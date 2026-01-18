package com.ey.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateSaleRequest {
    @NotNull
    private Long customerId;

    private boolean useWallet;

    @NotNull
    private List<SaleItemRequest> items;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public boolean isUseWallet() {
		return useWallet;
	}

	public void setUseWallet(boolean useWallet) {
		this.useWallet = useWallet;
	}

	public List<SaleItemRequest> getItems() {
		return items;
	}

	public void setItems(List<SaleItemRequest> items) {
		this.items = items;
	}

    
}
