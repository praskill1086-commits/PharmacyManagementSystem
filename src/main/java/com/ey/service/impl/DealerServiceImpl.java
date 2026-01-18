package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateDealerRequest;
import com.ey.dto.request.UpdateDealerRequest;
import com.ey.dto.response.DealerResponse;
import com.ey.entity.Dealer;
import com.ey.exception.DealerOperationException;
import com.ey.mapper.DealerMapper;
import com.ey.repository.DealerRepository;
import com.ey.service.DealerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DealerServiceImpl implements DealerService {

    private static final Logger logger =
            LoggerFactory.getLogger(DealerServiceImpl.class);

    @Autowired
    private DealerRepository repo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public DealerResponse create(CreateDealerRequest request) {
    	
    	 if (repo.existsByNameAndActiveTrue(request.getName())) {
         	logger.warn("Dealer already exists with name: {}", request.getName());
             throw new DealerOperationException("Dealer with this name already exists");
         }

        if (repo.existsByEmail(request.getEmail())) {
            logger.warn("Dealer already exists with email: {}", request.getEmail());
            throw new DealerOperationException("Dealer with this email already exists");
        }
        
        if (repo.existsByPhone(request.getPhone())) {
        	logger.warn("Dealer already exists with phone: {}", request.getPhone());
            throw new DealerOperationException("Dealer with this phone already exists");
        }

       

        Dealer dealer = DealerMapper.toEntity(request);
        dealer.setActive(true);
        dealer.setCreatedAt(LocalDateTime.now());
        dealer.setUpdatedAt(LocalDateTime.now());
        dealer.setUpdatedBy(currentUser.getCurrentUser());

        Dealer saved = repo.save(dealer);
        logger.info("Dealer created with id {}", saved.getId());

        return DealerMapper.toResponse(saved);
    }

    @Override
    public List<DealerResponse> getAll() {
        logger.info("Fetching all active dealers");
        return repo.findByActiveTrue()
                .stream()
                .map(DealerMapper::toResponse)
                .toList();
    }

    @Override
    public DealerResponse getById(Long id) {
        Dealer dealer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found"));

        return DealerMapper.toResponse(dealer);
    }

    @Override
    public DealerResponse update(Long id, UpdateDealerRequest request) {

        Dealer dealer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found"));
        
        if (!dealer.getName().equalsIgnoreCase(request.getName())
                && repo.existsByNameAndActiveTrue(request.getName())) {
            throw new DealerOperationException("Dealer name already exists");
        }
        
        if (!dealer.getEmail().equals(request.getEmail())
                && repo.existsByEmail(request.getEmail())) {
            throw new DealerOperationException("Email already in use");
        }

        if (!dealer.getPhone().equals(request.getPhone())
                && repo.existsByPhone(request.getPhone())) {
            throw new DealerOperationException("Phone already in use");
        }

        

        dealer.setName(request.getName());
        dealer.setEmail(request.getEmail());
        dealer.setPhone(request.getPhone());
        dealer.setAddress(request.getAddress());
        dealer.setActive(request.isActive());
        dealer.setUpdatedAt(LocalDateTime.now());
        dealer.setUpdatedBy(currentUser.getCurrentUser());

        Dealer saved = repo.save(dealer);
        logger.info("Dealer updated with id {}", saved.getId());

        return DealerMapper.toResponse(saved);
    }

    @Override
    public void disable(Long id) {

        Dealer dealer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found"));

        if (!dealer.isActive()) {
            throw new RuntimeException("Dealer already disabled");
        }

        dealer.setActive(false);
        dealer.setUpdatedAt(LocalDateTime.now());
        dealer.setUpdatedBy(currentUser.getCurrentUser());

        repo.save(dealer);
        logger.info("Dealer disabled with id {}", id);
    }
}
