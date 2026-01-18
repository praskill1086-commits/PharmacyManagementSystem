package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.OfferResponse;
import com.ey.entity.Offer;
import com.ey.repository.OfferRepository;
import com.ey.service.OfferService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepo;
    private final CurrentUserUtil currentUser;

    public OfferServiceImpl(OfferRepository offerRepo, CurrentUserUtil currentUser) {
        this.offerRepo = offerRepo;
        this.currentUser = currentUser;
    }

    @Override
    public OfferResponse create(CreateOfferRequest request) {

        Offer offer = new Offer();
        offer.setName(request.getName());
        offer.setDiscountPercent(request.getDiscountPercent());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(true);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());

        return toResponse(offerRepo.save(offer));
    }

    @Override
    public OfferResponse update(Long id, UpdateOfferRequest request) {

        Offer offer = offerRepo.findById(id).orElseThrow();

        offer.setName(request.getName());
        offer.setDiscountPercent(request.getDiscountPercent());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(request.isActive());
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());

        return toResponse(offerRepo.save(offer));
    }

    @Override
    public void disable(Long id) {
        Offer offer = offerRepo.findById(id).orElseThrow();
        offer.setActive(false);
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());
        offerRepo.save(offer);
    }

    @Override
    public OfferResponse getActiveOffer() {
        LocalDate today = LocalDate.now();
        Offer offer = offerRepo
                .findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .orElse(null);

        return offer != null ? toResponse(offer) : null;
    }

    @Override
    public List<OfferResponse> getAll() {
        return offerRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private OfferResponse toResponse(Offer o) {
        OfferResponse r = new OfferResponse();
        r.setId(o.getId());
        r.setName(o.getName());
        r.setDiscountPercent(o.getDiscountPercent());
        r.setStartDate(o.getStartDate());
        r.setEndDate(o.getEndDate());
        r.setActive(o.isActive());
        r.setCreatedAt(o.getCreatedAt());
        r.setUpdatedAt(o.getUpdatedAt());
        r.setUpdatedBy(o.getUpdatedBy() != null ? o.getUpdatedBy().getUsername() : null);
        return r;
    }
}
