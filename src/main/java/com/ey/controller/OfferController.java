package com.ey.controller;

import com.ey.dto.request.*;
import com.ey.dto.response.OfferResponse;
import com.ey.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService service;

    public OfferController(OfferService service) {
        this.service = service;
    }

    @PostMapping
    public OfferResponse create(@Valid @RequestBody CreateOfferRequest request) {
        return service.create(request);
    }

    @GetMapping("/active")
    public OfferResponse getActive() {
        return service.getActiveOffer();
    }

    @GetMapping
    public List<OfferResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public OfferResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOfferRequest request
    ) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        service.disable(id);
    }
}
