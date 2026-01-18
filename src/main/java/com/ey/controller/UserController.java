package com.ey.controller;

import com.ey.dto.request.*;
import com.ey.dto.response.*;
import com.ey.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        service.disable(id);
    }

    @PutMapping("/{id}/enable")
    public void enable(@PathVariable Long id) {
        service.enable(id);
    }
}
