package com.ey.controller;

import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.service.DealerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealers")
public class DealerController {

    private final DealerService service;

    public DealerController(DealerService service) {
        this.service = service;
    }

    @PostMapping
    public DealerResponse create(@Valid @RequestBody CreateDealerRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<DealerResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DealerResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public DealerResponse update(@PathVariable Long id, @Valid @RequestBody UpdateDealerRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        service.disable(id);
    }
}
