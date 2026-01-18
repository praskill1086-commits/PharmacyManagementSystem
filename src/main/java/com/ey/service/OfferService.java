package com.ey.service;

import com.ey.dto.request.*;
import com.ey.dto.response.OfferResponse;

import java.util.List;

public interface OfferService {

    OfferResponse create(CreateOfferRequest request);

    OfferResponse update(Long id, UpdateOfferRequest request);

    void disable(Long id);

    OfferResponse getActiveOffer();

    List<OfferResponse> getAll();
}
