package com.ey.service.impl;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateUserRequest;
import com.ey.dto.request.UpdateUserRequest;
import com.ey.dto.response.UserResponse;
import com.ey.entity.User;
import com.ey.exception.UserOperationException;
import com.ey.mapper.UserMapper;
import com.ey.repository.UserRepository;
import com.ey.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public UserResponse create(CreateUserRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            logger.warn("Username already exists: {}", request.getUsername());
            throw new UserOperationException("Username already exists");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            logger.warn("Email already exists: {}", request.getEmail());
            throw new UserOperationException("Email already exists");
        }

        User user = UserMapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(currentUser.getCurrentUser());

        User saved = userRepo.save(user);
        logger.info("User created with id {}", saved.getId());

        return UserMapper.toResponse(saved);
    }

    @Override
    public List<UserResponse> getAll() {
        logger.info("Fetching all users");
        return userRepo.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(Enum.valueOf(
                com.ey.enums.Role.class, request.getRole()));
        user.setActive(request.isActive());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(currentUser.getCurrentUser());

        User saved = userRepo.save(user);
        logger.info("User updated with id {}", saved.getId());

        return UserMapper.toResponse(saved);
    }

    @Override
    public void disable(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User already disabled");
        }

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(currentUser.getCurrentUser());

        userRepo.save(user);
        logger.info("User disabled with id {}", id);
    }

    @Override
    public void enable(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isActive()) {
            throw new RuntimeException("User already enabled");
        }

        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(currentUser.getCurrentUser());

        userRepo.save(user);
        logger.info("User enabled with id {}", id);
    }
}
