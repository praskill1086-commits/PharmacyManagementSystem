package com.ey.service;

import com.ey.dto.request.CreateDealerRequest;
import com.ey.dto.request.UpdateDealerRequest;
import com.ey.dto.response.DealerResponse;

import java.util.List;

public interface DealerService {
    DealerResponse create(CreateDealerRequest request);
    List<DealerResponse> getAll();
    DealerResponse getById(Long id);
    DealerResponse update(Long id, UpdateDealerRequest request);
    void disable(Long id);
}
