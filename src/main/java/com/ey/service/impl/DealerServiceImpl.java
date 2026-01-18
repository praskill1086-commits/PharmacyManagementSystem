package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.entity.Dealer;
import com.ey.mapper.DealerMapper;
import com.ey.repository.DealerRepository;
import com.ey.service.DealerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DealerServiceImpl implements DealerService {

    private final DealerRepository repo;
    private final CurrentUserUtil currentUser;

    public DealerServiceImpl(DealerRepository repo, CurrentUserUtil currentUser) {
        this.repo = repo;
        this.currentUser = currentUser;
    }

    @Override
    public DealerResponse create(CreateDealerRequest request) {
        Dealer d = DealerMapper.toEntity(request);
        d.setCreatedAt(LocalDateTime.now());
        d.setUpdatedAt(LocalDateTime.now());
        d.setUpdatedBy(currentUser.getCurrentUser());
        return DealerMapper.toResponse(repo.save(d));
    }

    @Override
    public List<DealerResponse> getAll() {
        return repo.findByActiveTrue().stream().map(DealerMapper::toResponse).toList();
    }

    @Override
    public DealerResponse getById(Long id) {
        return DealerMapper.toResponse(repo.findById(id).orElseThrow());
    }

    @Override
    public DealerResponse update(Long id, UpdateDealerRequest request) {
        Dealer d = repo.findById(id).orElseThrow();
        d.setName(request.getName());
        d.setEmail(request.getEmail());
        d.setPhone(request.getPhone());
        d.setAddress(request.getAddress());
        d.setActive(request.isActive());
        d.setUpdatedAt(LocalDateTime.now());
        d.setUpdatedBy(currentUser.getCurrentUser());
        return DealerMapper.toResponse(repo.save(d));
    }

    @Override
    public void disable(Long id) {
        Dealer d = repo.findById(id).orElseThrow();
        d.setActive(false);
        d.setUpdatedAt(LocalDateTime.now());
        d.setUpdatedBy(currentUser.getCurrentUser());
        repo.save(d);
    }
}
