package com.ey.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CreateSaleRequest;
import com.ey.dto.response.SaleResponse;
import com.ey.dto.response.SalesSummaryResponse;
import com.ey.service.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Create Sale (Bill)
    @PostMapping
    public SaleResponse createSale(@Valid @RequestBody CreateSaleRequest request) {
        return saleService.createSale(request);
    }

    // Get all sales
    @GetMapping
    public List<SaleResponse> getAllSales() {
        return saleService.getAllSales();
    }

    // Get sale by ID
    @GetMapping("/{id}")
    public SaleResponse getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }

    // Sales report by date range
    @GetMapping("/report")
    public List<SaleResponse> getSalesReport(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return saleService.getSalesBetween(from.atStartOfDay(), to.plusDays(1).atStartOfDay());
    }
    
    
    @GetMapping("/report/total")
    public double getSalesTotal(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return saleService.getTotalBetween(
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay()
        );
    }
    
    @GetMapping("/report/summary")
    public SalesSummaryResponse getSalesSummary(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return saleService.getSalesSummary(from, to);
    }


}
