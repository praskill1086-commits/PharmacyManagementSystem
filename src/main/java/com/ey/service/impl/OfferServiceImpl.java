package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.OfferResponse;
import com.ey.entity.Offer;
import com.ey.exception.OfferNotFoundException;
import com.ey.repository.OfferRepository;
import com.ey.service.OfferService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private static final Logger logger =
            LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public OfferResponse create(CreateOfferRequest request) {

        logger.info("Creating new offer: {}", request.getName());

        Offer offer = new Offer();
        offer.setName(request.getName());
        offer.setDiscountPercent(request.getDiscountPercent());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(true);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());

        Offer saved = offerRepo.save(offer);

        logger.info("Offer created with id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public OfferResponse update(Long id, UpdateOfferRequest request) {

        logger.info("Updating offer id={}", id);

        Offer offer = offerRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Offer not found for update, id={}", id);
                    return new OfferNotFoundException("Offer not found");
                });

        offer.setName(request.getName());
        offer.setDiscountPercent(request.getDiscountPercent());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(request.isActive());
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());

        Offer updated = offerRepo.save(offer);

        logger.info("Offer updated successfully, id={}", id);
        return toResponse(updated);
    }

    @Override
    public void disable(Long id) {

        logger.info("Disabling offer id={}", id);

        Offer offer = offerRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Offer not found for disable, id={}", id);
                    return new OfferNotFoundException("Offer not found");
                });

        offer.setActive(false);
        offer.setUpdatedAt(LocalDateTime.now());
        offer.setUpdatedBy(currentUser.getCurrentUser());
        offerRepo.save(offer);

        logger.info("Offer disabled successfully, id={}", id);
    }

    @Override
    public OfferResponse getActiveOffer() {

        LocalDate today = LocalDate.now();
        logger.info("Fetching active offer for date={}", today);

        Offer offer = offerRepo
                .findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .orElse(null);

        if (offer == null) {
            logger.info("No active offer found");
            return null;
        }

        logger.info("Active offer found id={}", offer.getId());
        return toResponse(offer);
    }

    @Override
    public List<OfferResponse> getAll() {

        logger.info("Fetching all offers");

        return offerRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Local mapper method is PERFECTLY FINE here
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
        r.setUpdatedBy(
                o.getUpdatedBy() != null ? o.getUpdatedBy().getUsername() : null
        );
        return r;
    }
}
