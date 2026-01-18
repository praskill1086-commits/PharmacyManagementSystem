package com.ey.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ey.dto.request.CreateSaleRequest;
import com.ey.dto.response.SaleResponse;
import com.ey.dto.response.SalesSummaryResponse;

public interface SaleService {
    SaleResponse createSale(CreateSaleRequest request);
    List<SaleResponse> getAllSales();
    SaleResponse getSaleById(Long id);
    List<SaleResponse> getSalesBetween(LocalDateTime from, LocalDateTime to);
	double getTotalBetween(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2);
	SalesSummaryResponse getSalesSummary(LocalDate from, LocalDate to);

}
