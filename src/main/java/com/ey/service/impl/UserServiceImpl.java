package com.ey.service.impl;

import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.entity.User;
import com.ey.mapper.UserMapper;
import com.ey.repository.UserRepository;
import com.ey.service.UserService;
import com.ey.config.CurrentUserUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final CurrentUserUtil currentUser;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder encoder, CurrentUserUtil currentUser) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.currentUser = currentUser;
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        User u = UserMapper.toEntity(request);
        u.setPassword(encoder.encode(request.getPassword()));
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        u.setUpdatedBy(currentUser.getCurrentUser());
        return UserMapper.toResponse(userRepo.save(u));
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepo.findAll().stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public UserResponse getById(Long id) {
        return UserMapper.toResponse(userRepo.findById(id).orElseThrow());
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        User u = userRepo.findById(id).orElseThrow();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setPhone(request.getPhone());
        u.setRole(com.ey.enums.Role.valueOf(request.getRole()));
        u.setActive(request.isActive());
        u.setUpdatedAt(LocalDateTime.now());
        u.setUpdatedBy(currentUser.getCurrentUser());
        return UserMapper.toResponse(userRepo.save(u));
    }

    @Override
    public void disable(Long id) {
        User u = userRepo.findById(id).orElseThrow();
        u.setActive(false);
        userRepo.save(u);
    }

    @Override
    public void enable(Long id) {
        User u = userRepo.findById(id).orElseThrow();
        u.setActive(true);
        userRepo.save(u);
    }
}
